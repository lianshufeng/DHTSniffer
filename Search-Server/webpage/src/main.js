var FileTypeICon = require('./FileTypeICon.js');



/**
 * 展示文件列表
 */
$(function ($) {
    var fileType = new FileTypeICon();
    var setFileItems = function (item, name) {
        var info = item[name];
        //没有子文件
        if (JSON.stringify(info) == '{}') {
            var extAt = name.lastIndexOf('.');
            var extName = extAt == -1 ? '' : name.substring(extAt + 1,
                name.length);
            info = {
                text: '<i class="ace-icon fa ' + fileType.get(extName)
                + '"></i> ' + name,
                type: 'item'
            }
            item[name] = info;
        } else {
            var itemNames = [];
            for (var i in info) {
                setFileItems(info, i);
                itemNames.push(i);
                // info['additionalParameters']['children'].push(info[i]);
                // delete info[i]
            }
            info['additionalParameters'] = {
                'children': []
            };
            info['text'] = name;
            info['type'] = 'folder';
            for (var i in itemNames) {
                info['additionalParameters']['children']
                    .push(info[itemNames[i]]);
                delete info[itemNames[i]]
            }
        }
    }

    var fileManager = $('#fileManager');
    fileManager
        .ace_tree({
            dataSource: initiateDemoData(fileManager),
            loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>',
            'open-icon': 'ace-icon fa fa-folder-open',
            'close-icon': 'ace-icon fa fa-folder',
            'itemSelect': false,
            'folderSelect': false,
            'multiSelect': false,
            'selected-icon': null,
            'unselected-icon': null,
            'folder-open-icon': 'ace-icon tree-plus',
            'folder-close-icon': 'ace-icon tree-minus'
        });

    //默认展开一层
    fileManager.tree('discloseVisible');

    function initiateDemoData(element) {
        // list转换字典对象
        var files = {};
        var lis = element.find('ul').children();
        for (var i = 0; i < lis.size(); i++) {
            //获取每一个文件名
            var filePath = $(lis.get(i)).html();
            //root级
            var currentFiles = files;
            //分割
            var filePath = filePath.split("/");
            for (var index in filePath) {
                var path = filePath[index];
                if (path == '') {
                    continue;
                }
                var item = currentFiles[path];
                if (item == null) {
                    item = {}
                    currentFiles[path] = item;
                }
                currentFiles = item;
            }
        }
        //构造为树形参数
        for (var name in files) {
            setFileItems(files, name);
        }
        var dataSource = function (options, callback) {
            var $data = null
            if (!("text" in options) && !("type" in options)) {
                $data = files;//the root tree
                callback({
                    data: $data
                });
                return;
            } else if ("type" in options && options.type == "folder") {
                if ("additionalParameters" in options
                    && "children" in options.additionalParameters)
                    $data = options.additionalParameters.children || {};
                else
                    $data = {}//no data
            }
            if ($data != null) {
                setTimeout(function () {
                    callback({
                        data: $data
                    });
                }, 0);
            }
        }
        return dataSource
    }
});


/**
 * 复制下载地址
 */
$(function () {
    $('#downLoadUrlDiv').find('[type="text"]').mousemove(function () {
        $(this).select();
    });
    //点击复制
    $('#downLoadUrlDiv').find('button').click(function () {
        let currentBtn = $(this);
        currentBtn.parent().parent().find("input").select();
        if (document.execCommand('copy')) {
            currentBtn.find("span").text("已复制");
        }
    });
});

/**
 * 热搜与搜索
 */
$(function () {
    var colors = ['btn-info', 'btn-danger', 'btn-yellow', 'btn-pink',
        'btn-purple', 'btn-inverse', 'btn-default', 'btn-grey',
        'btn-light'];
    var host = $('#hostServer').val();
    $.ajax({
        method: "post",
        dataType: "json",
        url: host + "store/hotWords.json",
        data: {
            count: 15
        },
        success: function (data) {
            if (data.invokerResult.content == null) {
                return false;
            }
            for (var i in data.invokerResult.content) {
                var item = data.invokerResult.content[i];
                $('#openLinkDiv').append(
                    '<button type="button" style="margin: 2px" class="btn btn-white  '
                    + colors[i % colors.length] + '">'
                    + item.name + '</button>');
            }
            //绑定事件
            $('#openLinkDiv').find('button').click(function () {
                var url = host + 'index.html#/s/' + $(this).html();
                window.open(url);
            });
        }

    });

});

// 右上角搜索功能
$(function () {
    var host = $('#hostServer').val();
    $('#searchNewWord').keydown(
        function (event) {
            if (event.keyCode == "13") {
                var url = host + 'index.html#/s/'
                    + $('#searchNewWord').val();
                window.open(url);
            }
        })
});

/**
 * 在线播放，暂不实现
 */
$(function () {
    let isShowPlayer  = false;
    let magnetUrl = $('#magnetUrlText').val();
    //判断下载地址是否磁力连
    if (magnetUrl.toLowerCase().indexOf("magnet:?xt=") == 0) {
        isShowPlayer = true;
    }

    //播放显示
    if (isShowPlayer){
        // $('#vedioPlayerContent').show();
    }

    if (isShowPlayer){

    }


});