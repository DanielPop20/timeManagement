$(document).ready(function() {
    let tasks = [];
    let categories = [];
    let calendar;

    function loadTasks() {
        $.ajax({
            url: 'tasks',
            method: 'GET',
            data: { action: 'fetchAll' },
            success: function(fetchedTasks) {
                tasks = fetchedTasks;
                updateTaskList(fetchedTasks);
            },
            error: function(xhr, status, error) {
                console.error("Error fetching tasks:", status, error);
            }
        });
    }

    function loadCategories() {
        $.ajax({
            url: 'tasks',
            method: 'GET',
            data: { action: 'allCategories' },
            success: function(fetchedCategories) {
                categories = fetchedCategories;
                populateCategoryDropdown();
            },
            error: function(xhr, status, error) {
                console.error("Error fetching categories:", status, error);
            }
        });
    }

    function populateCategoryDropdown() {
        let categorySelect = $('.category');
        categorySelect.empty();
        categorySelect.append('<option value="">Select Category</option>');
        categories.forEach(function(category) {
            categorySelect.append(`<option value="${category}">${category}</option>`);
        });
    }

     function filterTasks() {
            const selectedCategory = $('#category-filter').val();
            let filteredTasks = tasks;
            if (selectedCategory) {
                filteredTasks = tasks.filter(task => task.category === selectedCategory);
            }
            updateTaskList(filteredTasks);
    }

    function sortTasks(tasks) {
        return tasks.sort((a, b) => {
            const dateA = new Date(a.deadline.year, a.deadline.month - 1, a.deadline.day);
            const dateB = new Date(b.deadline.year, b.deadline.month - 1, b.deadline.day);
            return dateA - dateB;
        });
    }

    function updateTaskList(tasks) {
        updateCalendar(tasks);
        let taskContainer = $('#task-container');
        taskContainer.empty();

        tasks.forEach(function(task) {
            const taskHtml = `
                <div class="col-md-4">
                    <div class="card task-card" id="task-${task.id}">
                        <div class="card-body">
                            <h5 class="card-title">${task.title}</h5>
                            <p class="card-text">${task.description}</p>
                            <p class="card-text"><strong>Category:</strong> ${task.category}</p>
                            <p class="card-text"><strong>Deadline:</strong> ${task.deadline.year} - ${task.deadline.month} - ${task.deadline.day}</p>
                            <button class="btn btn-primary btn-edit" data-id="${task.id}">Edit</button>
                            <button class="btn btn-danger btn-delete" data-id="${task.id}">Delete</button>
                        </div>
                    </div>
                </div>`;
            taskContainer.append(taskHtml);
        });
    }

    function updateCalendar(tasks) {
        if (calendar) {
            calendar.removeAllEvents();
            calendar.addEventSource(tasks.map(task => ({
                id: task.id,
                title: task.title,
                start: new Date(task.deadline.year, task.deadline.month - 1, task.deadline.day),
                description: task.description
            })));
        } else {
            initializeCalendar();
        }
    }

    function initializeCalendar() {
        const calendarEl = document.getElementById('calendar');
        if (!calendarEl) {
            console.error("Calendar container not found");
            return;
        }

        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            editable: true,
            events: tasks.map(task => ({
                id: task.id,
                title: task.title,
                start: new Date(task.deadline.year, task.deadline.month - 1, task.deadline.day),
                description: task.description
            })),
            eventContent: function(arg) {
                const { event } = arg;
                return {
                    html: `<div><strong>${event.title}</strong></div>`
                };
            },
            dateClick: function(info) {
                alert('Date: ' + info.dateStr);
            }
        });

        calendar.render();
    }

    function formatDateForInput(date) {
        if(!date) {
            return;
        }
        const year = date.year;
        const month = date.month < 10 ? '0' + date.month : date.month;
        const day = date.day < 10 ? '0' + date.day : date.day;
        return `${year}-${month}-${day}`;
    }

    function showTaskModal(mode, task = {}) {
        $('#id').val(task.id || '');
        $('#title').val(task.title || '');
        $('#description').val(task.description || '');
        $('#category').val(task.category || '');
        $('#deadline').val(formatDateForInput(task.deadline || ''));

        if (mode === 'edit') {
            $('#modal-title').text('Edit Task');
            $('#submit-button').text('Update Task');
            $('#task-form').attr('action', 'update');
        } else {
            $('#modal-title').text('Add New Task');
            $('#submit-button').text('Add Task');
            $('#task-form').attr('action', 'add');
        }

        $('#task-modal').modal('show');
    }

    $('#add-task-button').on('click', function() {
        showTaskModal('add');
    });

    $(document).on('click', '.btn-edit', function() {
        const taskId = $(this).data('id');
        $.ajax({
            url: 'tasks',
            method: 'GET',
            data: { action: 'edit', taskId: taskId },
            success: function(response) {
                if (response.error) {
                    console.error("Error fetching task details:", response.error);
                } else {
                    showTaskModal('edit', response);
                }
            },
            error: function(xhr, status, error) {
                console.error("Error fetching task details:", status, error);
            }
        });
    });

    $('#task-form').on('submit', function(e) {
        e.preventDefault();
        const action = $(this).attr('action');
        $.ajax({
            url: 'tasks',
            method: 'POST',
            data: $(this).serialize() + '&action=' + action,
            success: function(response) {
                if (response.status === 'success') {
                    loadTasks();
                    $('#task-modal').modal('hide');
                    alert('Task ' + (action === 'add' ? 'added' : 'updated') + ' successfully!');
                } else {
                    console.error("Error " + (action === 'add' ? 'adding' : 'updating') + " task:", response.error);
                }
            },
            error: function(xhr, status, error) {
                console.error("Error " + (action === 'add' ? 'adding' : 'updating') + " task:", status, error);
            }
        });
    });

    $(document).on('click', '.btn-delete', function() {
        if (confirm('Are you sure you want to delete this task?')) {
            const taskId = $(this).data('id');
            $.ajax({
                url: 'tasks',
                method: 'POST',
                data: { action: 'delete', taskId: taskId },
                success: function(response) {
                    if (response.status === 'success') {
                        loadTasks();
                        alert('Task deleted successfully!');
                    } else {
                        console.error("Error deleting task:", response.error);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error deleting task:", status, error);
                }
            });
        }
    });

    $('#apply-filter').on('click', filterTasks);

    loadCategories();
    loadTasks();
});
