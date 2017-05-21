/**
 * Created by wwwtm on 19.05.2017.
 */
var ajaxUrl = 'ajax/profile/meals';
var datatableApi;

var pageContextUpdateTableQuery = function () {
    return "?" + $('#filterForm').serialize();
};

var resetFilter = function () {
    $("#filterForm").find("input").each(function () {
        $(this).val("")
    });
};

// $(document).ready(function () {
$(function () {
    datatableApi = $('#datatable').DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
                "desc"
            ]
        ]
    });
    makeEditable();
});