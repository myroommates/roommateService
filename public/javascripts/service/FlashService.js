myApp.service("$flash", function($filter) {

    Messenger.options = {
        extraClasses: 'messenger-fixed messenger-on-top messenger-on-center cr-messenger',
        theme: 'block'
    }

    this.success = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'success',
                showCloseButton: true
            });
        }

        return;
    };
    this.info = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'info',
                showCloseButton: true
            });
        }
    };
    this.error = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'error',
                showCloseButton: true
            });
        }

        return;

    };
    this.warning = function(messages) {
        for(var key in messages.split("\n")){
            var message = messages.split("\n")[key];

            Messenger().post({
                message: message,
                type: 'warning',
                showCloseButton: true
            });
        }

        return;
    };
});
