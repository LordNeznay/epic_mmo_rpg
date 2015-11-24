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


        events: {
            "submit .form-registration": "submitSignup",
            "click a": "hide"
        },
        show: function () {
            View.__super__.show.call(this);
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
                var values = $('.form-registration').serialize();
                this.player.registration(values, {
                    success: function(data) {
                        if(data.errors == 'null'){
                            pView.player.login(values);
                            Backbone.history.navigate('login', true);
                        } else {
                            $(".form-registration .form__errors").html(data.errors);
                        }
                    }
                });
            }
            return false;
        }
    });

    function validateForm(){
        var userName = $(".form-registration input[name=name]").val();
        if (userName=='') {
            $('.form-registration .form__errors').text("Какая-то жуткая ошибка! Так делать нельзя!");
            return false;
        }
        var userPassword = $(".form-registration input[name=password]").val();
        if (userPassword=='') {
            $('.form-registration .form__errors').text("Какая-то жуткая ошибка! Так делать нельзя!");
            return false;
        }
        return true;
    }
    function clearErrors(){
        $('.form-registration .form__errors').text("");
    }

    return new View({content: '.page-registration'});
});