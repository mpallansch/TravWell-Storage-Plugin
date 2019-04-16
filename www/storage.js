alert('Installing plugin!');

window.getPreviousStorage = function(str, callback) {
    cordova.exec(callback, function(err) {
        callback('Error fetching old data.');
    }, "Storage", "getPreviousStorage", [str]);
};
 