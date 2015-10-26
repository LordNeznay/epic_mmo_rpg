define([
    'backbone',
    'tmpl/registration',
    'views/base'
], function(
    Backbone,
    tmpl,
    BaseView
){
    

    var View = BaseView.extend({
        name: 'registration',
        template: tmpl,
        className: "login-registration",

        events: {
            "submit .signup-form__form": "submitSignup",
            "click a": "hide"
        },
        child_init: function () {
            $.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){	
                    if(data.isLogin == 'true'){
                        Backbone.history.navigate('login', true);
                    }
                }
            });
        },
        submitSignup: function(){
            clearErrors();
            if(validateForm()){
                var pView = this;
                var values = $('.signup-form__form').serialize();
                this.player.registration(values, {
                    success: function(data) {
                        if(data.errors == 'null'){
                            pView.player.login(values);
                            Backbone.history.navigate('login', true);
                        } else {
                            $(".signup-form__errors").html(data.errors);
                        }
                    }
                });
            }
            return false;
        }
    });

    function validateForm(){
        var userName = $("input[name=name]").val();
        if (userName=='') {
            $('.signup-form__errors').text("Какая-то жуткая ошибка! Так делать нельзя!");
            return false;
        }
        var userPassword = $("input[name=password]").val();
        if (userPassword=='') {
            $('.signup-form__errors').text("Какая-то жуткая ошибка! Так делать нельзя!");
            return false;
        }
        return true;
    }
    function clearErrors(){
        $('.signup-form__errors').text("");
    }

    return new View({content: '.page-registration'});
});