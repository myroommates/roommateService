myApp.service("$flash", function($filter) {

    Messenger.options = {
        extraClasses: 'messenger-fixed messenger-on-top messenger-on-center cr-messenger',
        theme: 'block'
    }

    this.success = function(key) {
        return Messenger().post({
            message: key,
            type: 'success',
            showCloseButton: true
        });
    };
    this.info = function(key) {
        return Messenger().post({
            message: key,
            type: 'info',
            showCloseButton: true
        });
    };
    this.error = function(key) {
        return Messenger().post({
            message: key,
            type: 'error',
            showCloseButton: true
        });
    };
    this.warning = function(key) {
        return Messenger().post({
            message: key,
            type: 'warning',
            showCloseButton: true
        });
    };
});
