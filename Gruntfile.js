module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        cssmin: {
            generated: {
                files: [
                    { dest: 'public/dist/dependencies.min.css',
                        src: [ 'public/dist/dependencies.css' ] }
                ]
            }
        },
        concat: {
            generated: {
                files: [
                    /*
                    {
                        dest: 'public/dist/dependencies.js',

                        src: [
                            'public/components/bootstrap/dist/js/bootstrap.min.js',
                            'public/components/messenger/build/js/messenger.min.js',
                            'public/components/moment/min/moment.min.js',
                            'public/components/angular-bootstrap/ui-bootstrap.min.js',
                            'public/components/angular-bootstrap/ui-bootstrap-tpls.min.js',
                            'public/components/angucomplete/angucomplete.js',
                            'public/components/underscore/underscore-min.js',
                            'public/components/mathjs/dist/math.min.js',
                            'public/components/angular-bootstrap-datetimepicker/src/js/datetimepicker.js',
                            'public/components/angular-i18n/angular-locale_fr-fr.js',
                            'public/components/angular-dynamic-locale/src/tmhDynamicLocale.js',
                            'public/components/bootstrap/js/transition.js',
                            'public/components/bootstrap/js/collapse.js',
                            'public/javascripts/jquery-touchswipe.js',
                            'public/javascripts/menu-animation.js']
                    },*/

                    {
                        dest: 'public/dist/dependencies.css',

                        src: [  'public/components/angucomplete/angucomplete.css',
                            'public/components/bootstrap/dist/css/bootstrap.min.css',
                            'public/components/bootstrap/dist/css/bootstrap-theme.min.css',
                            'public/components/messenger/build/css/messenger.css',
                            'public/components/angular-bootstrap-datetimepicker/src/css/datetimepicker.css',
                            'public/components/eonasdan-bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.min.css']
                    }
                ]
            }
        },
        uglify: {
            my_target: {
                files: {
                    //'public/dist/dependencies.min.js': ['public/dist/dependencies.js'],
                    'public/dist/angular.min.js': ['public/javascripts/dist/angular.js']
                }
            }
        },
        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            myApp: {
                files: {
                    'public/javascripts/dist/angular.js': [
                        'public/javascripts/controller/MainCtrl.js',
                        'public/javascripts/controller/HomeCtrl.js',
                        'public/javascripts/controller/AdminRoommateListCtrl.js',
                        'public/javascripts/controller/AdminPreferenceCtrl.js',
                        'public/javascripts/controller/LateralMenuCtrl.js',
                        'public/javascripts/controller/CountResumeCtrl.js',
                        'public/javascripts/controller/CountTicketCtrl.js',
                        'public/javascripts/controller/ProfileMyProfileCtrl.js',
                        'public/javascripts/controller/ShoppingListCtrl.js',
                        'public/javascripts/controller/RegistrationCtrl.js',
                        'public/javascripts/controller/AboutCtrl.js',
                        'public/javascripts/modal/CUTicket/CuTicketCtrl.js',
                        'public/javascripts/modal/CUShoppingItem/CuShoppingItemCtrl.js',
                        'public/javascripts/modal/HelpModal/HelpModalCtrl.js',
                        'public/javascripts/modal/ChangeEmail/ChangeEmailModal.js',
                        'public/javascripts/modal/ChangePassword/ChangePasswordModal.js',
                        'public/javascripts/modal/Calculator/CalculatorModalCtrl.js',
                        'public/javascripts/directive/dirFieldDate/directive.js',
                        'public/javascripts/directive/dirFieldText/directive.js',
                        'public/javascripts/directive/dirInputNumber/directive.js',
                        'public/javascripts/directive/dirUserIcon/directive.js',
                        'public/javascripts/directive/dirFocusMe/directive.js',
                        'public/javascripts/directive/dirEnter/directive.js',
                        'public/javascripts/service/FlashService.js',
                        'public/javascripts/service/DirectiveService.js',
                        'public/javascripts/service/GenerateIdService.js',
                        'public/javascripts/service/TranslationService.js',
                        'public/javascripts/filter/TranslateTextFilter.js']
                }
            }
        }
    });

    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-usemin');


    // Default task(s).
    grunt.registerTask('default', ['concat',
        'ngAnnotate',
        'uglify',
        'cssmin']);

};
