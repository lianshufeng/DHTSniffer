/**
 * 文件类型对象
 * @constructor
 */
var FileTypeICon = function () {
    var items = {
        '': 'fa-file brown',
        'jpg': 'fa-picture-o green',
        'bmp': 'fa-picture-o green',
        'gif': 'fa-picture-o green',

        'mp3': 'fa-music blue',
        'wav': 'fa-music blue',
        'wmv': 'fa-music blue',

        'mp4': 'fa-film blue',
        'mkv': 'fa-film blue',
        'rmvb': 'fa-film blue',
        'rm': 'fa-film blue',
        'avi': 'fa-film blue',

        'txt': 'fa-file-text grey',
        'html': 'fa-file-text grey',
        'pdf': 'fa-file-text grey',
        'doc': 'fa-file-text grey',
        'docx': 'fa-file-text grey',
        'xls': 'fa-file-text grey',
        'xlsx': 'fa-file-text grey',
        'ppt': 'fa-file-text grey',
        'pptx': 'fa-file-text grey',

        'zip': 'fa-archive brown',
        '7z': 'fa-archive brown',
        'rar': 'fa-archive brown',
    };
    /**
     * 取出文件类型
     * @param type
     * @returns {string}
     */
    this.get = function (type) {
        var val = items[type.toLowerCase()];
        return val == null ? items[''] : val;
    }
}

module.exports = FileTypeICon;