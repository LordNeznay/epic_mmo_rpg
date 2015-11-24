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


        events: {
            "submit .form-login": "submitLogin",
            "submit .form-unlogin": "submitUnlogin",
            "click a": "hide"
        },
        show: function () {
            View.__super__.show.call(this);
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
                this.player.login($('.form-login').serialize(), {
                    success: function(data){
                        if(data.errors == 'null'){
                            Backbone.history.navigate('', true);
                        } else {
                            $(".form-login .form__errors").html(data.errors);
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
        var userName = $(".form-login input[name=name]").val();
        if (userName=='') {
            $('.form-login .form__errors').text("Введите имя!");
            return false;
        }
        var userPassword = $(".form-login input[name=password]").val();
        if (userPassword=='') {
            $('.form-login .form__errors').text("Введите пароль!");
            return false;
        }
        return true;
    }
    function clearErrors(){
        $('.form-login .form__errors').text("");
    }

    return new View({content: '.page-login'});
});