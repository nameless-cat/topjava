var ajaxUrl = 'ajax/admin/users';
var datatableApi;

var pageContextEditable = function () {
    $('.enabled').click(function () {
        var data = {
            enabled: $(this).is(':checked'),
            id: $(this).parents("tr").attr("id")
        };

        $.ajax({
            url: ajaxUrl + '?' + $.param(data),
            method: 'PUT',
            success: function () {
                updateTable();
                successNoty('Status changed');
            }
        });
    });
};

// $(document).ready(function () {
$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});