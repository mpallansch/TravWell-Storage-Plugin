alert('Installing plugin!');

window.getPreviousStorage = function(str, callback) {
    alert('Plugin received method call!');
    cordova.exec(callback, function(err) {
        callback('Error fetching old data.');
    }, "Storage", "getPreviousStorage", [str]);
};
 