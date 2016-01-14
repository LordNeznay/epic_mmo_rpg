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
        render: function () {
            View.__super__.render.call(this);
            this.el_login_form__div = $(".login-form");
            this.el_login_form = $(".form-login");
            this.el_unlogin_form__div = $(".unlogin-form");
            this.el_login_errors = $(".form-login .form__errors");
            this.el_input_login = $(".form-login input[name=name]");
            this.el_input_password = $(".form-login input[name=password]");
        },
        show: function () {
            View.__super__.show.call(this);
            this.player.status();
            if(this.player.isLogin){
                this.el_login_form__div.hide();
                this.el_unlogin_form__div.show();
            } else {
                this.el_unlogin_form__div.hide();
                this.el_login_form__div.show();
            }
        },
        submitLogin: function(event){
            this.clearErrors();
            if(this.validateForm()){
                var pView = this;	
                this.player.login(pView.el_login_form.serialize(), {
                    success: function(data){
                        if(data.errors == 'null'){
                            Backbone.history.navigate('', true);
                        } else {
                            pView.el_login_errors.html(data.errors);
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
        },

        validateForm: function(){
            var userName = this.el_input_login.val();
            if (userName=='') {
                this.el_login_errors.text("Введите имя!");
                return false;
            }
            var userPassword = this.el_input_password.val();
            if (userPassword=='') {
                this.el_login_errors.text("Введите пароль!");
                return false;
            }
            return true;
        }, 
        clearErrors: function(){
            this.el_login_errors.text("");
        }
        
    });
    
    return new View({content: '.page-login'});
});