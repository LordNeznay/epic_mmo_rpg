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
        render: function () {
            View.__super__.render.call(this);
            this.el_registration_form = $(".form-registration");
            this.el_registration_form__errors = $('.form-registration .form__errors');
            this.el_registration_login = $(".form-registration input[name=name]");
            this.el_registration_password = $(".form-registration input[name=password]");
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
            this.clearErrors();
            if(this.validateForm()){
                var pView = this;
                var values = this.el_registration_form.serialize();
                this.player.registration(values, {
                    success: function(data) {
                        if(data.errors == 'null'){
                            pView.player.login(values);
                            Backbone.history.navigate('login', true);
                        } else {
                            pView.el_registration_form__errors.html(data.errors);
                        }
                    }
                });
            }
            return false;
        },
    
        validateForm: function(){
            var userName = this.el_registration_login.val();
            if (userName=='') {
                this.el_registration_form__errors.text("Какая-то жуткая ошибка! Так делать нельзя!");
                return false;
            }
            var userPassword = this.el_registration_password.val();
            if (userPassword=='') {
                this.el_registration_form__errors.text("Какая-то жуткая ошибка! Так делать нельзя!");
                return false;
            }
            return true;
        },
        
        clearErrors: function(){
            this.el_registration_form__errors.text("");
        }
    });
    
    return new View({content: '.page-registration'});
});