var ajaxUrl = 'ajax/admin/users';
var datatableApi;

var pageContextEditable = function () {
    $('.enabled').click(function () {
        $.ajax({
            url: ajaxUrl + _getRestOfUri(this),
            method: 'PUT',
            success: function () {
                updateTable();
                successNoty('Status changed');
            }
        });
    });
};

var _getRestOfUri = function (element) {
    var data = {enabled: $(element).is(':checked')};
    var id = $(element).parents("tr").attr("id");

    return "/" + id + "?" + $.param(data);
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