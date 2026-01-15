$(function() {
    initDatePickers();
    var panehHidden = false;
    if ($(this).width() < 769) {
        panehHidden = true;
    }
    $('body').layout({ initClosed: panehHidden, west__size: 185 });
    if ($.fn.toTop !== undefined) {
        var opt = {
            win:$('.ui-layout-center'),
            doc:$('.ui-layout-center')
        };
        $('#scroll-up').toTop(opt);
    }
    loadPostData();
    queryUserList();
    queryDeptTree();
});

function initDatePickers() {
    $("#startDate").datetimepicker({
        format: "yyyy-mm-dd",
        minView: "month",
        autoclose: true
    });
    $("#endDate").datetimepicker({
        format: "yyyy-mm-dd",
        minView: "month",
        autoclose: true
    });

    if (!initialStartDate || initialStartDate === 'null') {
        var now = new Date();
        var firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
        $("#startDate").val(formatDate(firstDay));
    }
    if (!initialEndDate || initialEndDate === 'null') {
        $("#endDate").val(formatDate(new Date()));
    }
}

function formatDate(date) {
    var year = date.getFullYear();
    var month = (date.getMonth() + 1).toString().padStart(2, '0');
    var day = date.getDate().toString().padStart(2, '0');
    return year + "-" + month + "-" + day;
}

function loadPostData() {
    $.get(ctx + "performance/post/list2", function(result) {
        if (result.code === 0) {
            var postOptions = '<option value="">请选择岗位</option>';
            $.each(result.rows, function(index, item) {
                postOptions += '<option value="' + item.postId + '">' + item.postName + '</option>';
            });
            $("#postIdSelect").html(postOptions);
            if ($("#postIdSelect").data("value")) {
                $("#postIdSelect").val($("#postIdSelect").data("value"));
            }
        }
    });
}

function queryUserList() {
    var url = dailyMode ? prefix + "/overview/dailyList" : prefix + "/overview/list";
    var options = {
        url: url,
        modalName: "绩效统计",
        pageNumber: initialPageNum || 1,
        pageSize: initialPageSize || 10,
        sortName: initialSortName || "",
        sortOrder: initialSortOrder || "",
        columns: [{
            checkbox: false,
            visible: false
        },
            {
                field: 'userId',
                title: '员工ID',
                visible: false
            },
            {
                field: 'userName',
                title: '员工姓名'
            },
            {
                field: 'deptId',
                title: '部门ID',
                visible: false
            },
            {
                field: 'deptName',
                title: '所属单位'
            },
            {
                field: 'postName',
                title: '岗位'
            },
            {
                field: 'postId',
                title: '岗位ID',
                visible: false
            },
            {
                field: 'gatherDate',
                title: '采集日期',
                formatter: function(value, row, index) {
                    if (dailyMode) {
                        return value || '';
                    }
                    var start = $("#startDate").val();
                    var end = $("#endDate").val();
                    return start && end ? (start + " ~ " + end) : '';
                }
            },
            {
                field: 'totalScore',
                title: '考核得分',
                sortable: true,
                formatter: function(value, row, index) {
                    var rawScore = parseFloat(value || 0);
                    var score = dailyMode ? rawScore : rawScore + 100;
                    var text = '<span class="score-' + (score >= 100 ? 'high' : score >= 80 ? 'medium' : 'low') + '">' + score.toFixed(1) + '</span>';
                    if (!dailyMode) {
                        return '<a href="javascript:void(0)" onclick="showDaily(' + row.userId + ', \'' + row.userName + '\')">' + text + '</a>';
                    }
                    return text;
                }
            },
            {
                title: '操作',
                align: 'center',
                formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-info btn-xs" href="javascript:void(0)" onclick="viewDetail(' + row.userId + ', \'' + row.gatherDate + '\')"><i class="fa fa-search"></i>查看</a> ');
                    return actions.join('');
                }
            }]
    };
    $.table.init(options);
}

function queryDeptTree()
{
    var url = ctx + "system/dept/treeData";
    var options = {
        url: url,
        expandLevel: 2,
        onClick : zOnClick
    };
    $.tree.init(options);

    function zOnClick(event, treeId, treeNode) {
        $("#deptId").val(treeNode.id);
        $("#parentId").val(treeNode.pId);
        $.table.search();
    }
}

$('#btnExpand').click(function() {
    $._tree.expandAll(true);
    $(this).hide();
    $('#btnCollapse').show();
});

$('#btnCollapse').click(function() {
    $._tree.expandAll(false);
    $(this).hide();
    $('#btnExpand').show();
});

$('#btnRefresh').click(function() {
    queryDeptTree();
});

function dept() {
    var url = ctx + "system/dept";
    $.modal.openTab("部门管理", url);
}

function buildQueryParams() {
    var params = $.common.formToJSON("statistics-form");
    var tableOptions = $("#bootstrap-table").bootstrapTable('getOptions');
    params.pageNum = tableOptions.pageNumber;
    params.pageSize = tableOptions.pageSize;
    params.sortName = tableOptions.sortName || tableOptions.sort;
    params.sortOrder = tableOptions.sortOrder || tableOptions.order;
    return $.param(params);
}

function showDaily(userId, userName) {
    var params = buildQueryParams();
    var url = prefix + "/overview/daily?userId=" + userId + "&userName=" + encodeURIComponent(userName || '') + "&returnParams=" + encodeURIComponent(params) + "&" + params;
    location.href = url;
}

function returnOverview() {
    var params = returnParams && returnParams !== 'null' ? returnParams : buildQueryParams();
    var url = prefix + "/overview?" + params;
    location.href = url;
}

function viewDetail(userId, gatherDate) {
    var params = buildQueryParams();
    var detailUrl;
    if (dailyMode) {
        detailUrl = prefix + "/detail?userId=" + userId + "&gatherDate=" + gatherDate + "&" + params;
    } else {
        var start = $("#startDate").val();
        var end = $("#endDate").val();
        detailUrl = prefix + "/detail?userId=" + userId + "&startDate=" + start + "&endDate=" + end + "&" + params;
    }
    $.modal.openTab("绩效统计详情", detailUrl);
}
