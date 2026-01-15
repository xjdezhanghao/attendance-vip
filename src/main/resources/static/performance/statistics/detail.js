$(document).ready(function() {
    $('.tab-pane').each(function() {
        var tabIndex = $(this).attr('id').replace('tab-', '');
        generateCategoryStructure(tabIndex);
        calculateTotals(tabIndex);
    });
});

function generateCategoryStructure(tabIndex) {
    var originalData = $('#originalData_' + tabIndex);
    var categoryContainer = $('#tab-' + tabIndex + ' .category-container');

    var categoryMap = {};
    originalData.find('.item-detail').each(function() {
        var categoryId = $(this).data('category-id');
        var categoryName = $(this).data('category-name');

        if (!categoryMap[categoryId]) {
            categoryMap[categoryId] = {
                id: categoryId,
                name: categoryName,
                items: []
            };
        }

        categoryMap[categoryId].items.push({
            itemId: $(this).data('item-id'),
            itemName: $(this).data('item-name'),
            ruleDesc: $(this).data('rule-desc'),
            scoreMin: $(this).data('score-min'),
            scoreMax: $(this).data('score-max'),
            itemScore: $(this).data('item-score'),
            itemRemark: $(this).data('item-remark'),
            imagePath: $(this).data('item-image-path'),
            scoreType: $(this).data('score-type')
        });
    });

    for (var categoryId in categoryMap) {
        var category = categoryMap[categoryId];
        var categoryScoreType = category.items.length > 0 ? category.items[0].scoreType : '0';

        var categorySection = $('<div class="category-section" data-category-id="' + category.id + '" data-tab-index="' + tabIndex + '" data-score-type="' + categoryScoreType + '"></div>');
        var categoryTypeText = '';
        if (categoryScoreType === '1' || categoryScoreType === 1) {
            categoryTypeText = ' (加分项)';
        } else if (categoryScoreType === '2' || categoryScoreType === 2) {
            categoryTypeText = ' (减分项)';
        }

        var categoryHeader = $('<div class="category-header">' + category.name + categoryTypeText +
            ' <span class="category-total">小计：<span id="categoryTotal_' + category.id + '_' + tabIndex + '">0</span></span></div>');

        var categoryBody = $('<div class="category-body"></div>');
        var table = $('<table class="item-table"></table>');
        var thead = $('<thead><tr><th width="25%">考核项目</th><th width="10%">得分</th><th width="35%">评分标准</th><th width="20%">备注</th><th width="10%">图片</th></tr></thead>');
        var tbody = $('<tbody></tbody>');

        for (var i = 0; i < category.items.length; i++) {
            var item = category.items[i];
            var row = $('<tr></tr>');
            row.append('<td>' + item.itemName + '</td>');
            var scoreValue = item.itemScore;
            var scoreText = (scoreValue === 0 || scoreValue) ? scoreValue : '-';
            row.append('<td class="score-cell" data-score="' + (scoreValue || 0) + '" data-score-type="' + item.scoreType + '">' + scoreText + '</td>');
            row.append('<td>' + (item.ruleDesc || '') + '</td>');
            row.append('<td>' + (item.itemRemark || '') + '</td>');

            var imageCell = $('<td></td>');
            if (item.imagePath) {
                var previewHtml = '<img src="' + item.imagePath + '" alt="预览图" onclick="previewImage(this)" class="thumbnail-img"/>';
                imageCell.append(previewHtml);
            }
            row.append(imageCell);
            tbody.append(row);
        }

        table.append(thead).append(tbody);
        categoryBody.append(table);
        categorySection.append(categoryHeader).append(categoryBody);
        categoryContainer.append(categorySection);
    }
}

function calculateTotals(tabIndex) {
    $('#tab-' + tabIndex + ' .category-section').each(function() {
        var total = 0;
        var scoreType = $(this).data('score-type');
        var signedMode = detailMode === 'range';

        $(this).find('.score-cell').each(function() {
            var score = parseFloat($(this).data('score')) || 0;
            if (signedMode) {
                total += score;
                return;
            }
            if (scoreType === '1' || scoreType === 1) {
                total += score;
            } else if (scoreType === '2' || scoreType === 2) {
                total -= score;
            }
        });

        var categoryId = $(this).data('category-id');
        $('#categoryTotal_' + categoryId + '_' + tabIndex).text(total.toFixed(1));
    });
}

function previewImage(img) {
    var src = $(img).attr('src');
    var modalHtml = '<div class="modal fade" id="imagePreviewModal" tabindex="-1" role="dialog">' +
        '<div class="modal-dialog modal-lg" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h4 class="modal-title">图片预览</h4>' +
        '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
        '</div>' +
        '<div class="modal-body text-center">' +
        '<img src="' + src + '" style="max-width: 100%; max-height: 70vh;"/>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    $('body').append(modalHtml);
    $('#imagePreviewModal').modal('show');
    $('#imagePreviewModal').on('hidden.bs.modal', function () {
        $(this).remove();
    });
}
