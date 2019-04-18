var exec = require('cordova/exec');

exports.getPreviousStorage = function(str, callback) {
    exec(callback, function(err) {
        callback('Error fetching old data. ' + err);
    }, "Storage", "getPreviousStorage", [str]);
}