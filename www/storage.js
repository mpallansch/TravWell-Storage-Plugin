alert('Installing plugin!');

var exec = require('cordova/exec');

exports.getPreviousStorage = function(str, callback) {
    alert('Plugin received method call!');
    cordova.exec(callback, function(err) {
        callback('Error fetching old data. ' + err);
    }, "Storage", "getPreviousStorage", [str]);
}