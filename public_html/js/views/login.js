define([
    'backbone',
    'tmpl/login',
    'views/base'
], function(
    Backbone,
    tmpl,
    BaseView
){

    var View = BaseView.extend({
        name: 'login',
        template: tmpl,
        className: "login-view",

        events: {
            "submit .login-form__form": "submitLogin",
            "submit .unlogin-form__form": "submitUnlogin",
            "click a": "hide"
        },
        child_show: function () {
            this.player.status();
            if(this.player.isLogin){
                $(".login-form").hide();
                $(".unlogin-form").show();
            } else {
                $(".unlogin-form").hide();
                $(".login-form").show();
            }
        },
        submitLogin: function(event){
            clearErrors();
            if(validateForm()){
                var pView = this;	
                this.player.login($('.login-form__form').serialize(), {
                    success: function(data){
                        if(data.errors == 'null'){
                            Backbone.history.navigate('main', true);
                        } else {
                            $(".login-form__errors").html(data.errors);
                        }
                    }
                });
            }
            return false;
        },
        submitUnlogin: function(event){
            var pView = this;
            this.player.unlogin({
                success: function(data){
                    pView.show();
                }
            });
            return false;
        }
    });

    function validateForm(){
        /*
        var userName = $("input[name=name]").val();
        if (userName=='') {
            $('.login-form__errors').text("Введите имя!");
            return false;
        }
        var userPassword = $("input[name=password]").val();
        if (userPassword=='') {
            $('.login-form__errors').text("Введите пароль!");
            return false;
        }*/
        return true;
    }
    function clearErrors(){
        $('.login-form__errors').text("");
    }

    return new View({content: '.page-login'});
});