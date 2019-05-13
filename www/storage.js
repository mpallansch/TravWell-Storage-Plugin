cordova.define("old-storage-plugin.Storage", function(require, exports, module) {
var exec = require('cordova/exec');

exports.getPreviousStorage = function(str, callback) {
    exec(function(response){
        try {
            response = JSON.parse(response.replace(/[\n|\r]/g, ''));
            callback(response);
        } catch (e) {
            callback('Error fetching old data. Unable to parse JSON. ' + response);
        }
    }, function(err) {
        callback('Error fetching old data. ' + err);
    }, "Storage", "getPreviousStorage", [str]);
}
});
