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
            $.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){	
                    if(data.isLogin == 'true'){
                        $(".login-form").hide();
                        $(".unlogin-form").show();
                    } else {
                        $(".unlogin-form").hide();
                        $(".login-form").show();
                    }
                }
            });
        },
        submitLogin: function(event){
            clearErrors();
            if(validateForm()){
                var pView = this;	
                $.ajax({
                    type: "POST",
                    data: $('.login-form__form').serialize(),
                    url: "/api/v1/auth/signin",
                    dataType: 'json',
                    success: function(data) {
                        if(data.errors == 'null'){
                            pView.show();
                        } else {
                            //Auaiaei ioeaee
                            $(".login-form__errors").html(data.errors);
                        }
                    }
                });
            }
            return false;
        },
        submitUnlogin: function(event){
            var pView = this;
            $.ajax({
                type: "POST",
                url: "/api/v1/auth/exit",
                success: function(data){
                    pView.show();
                }
            });    
            return false;
        }
    });

    function validateForm(){
        var userName = $("input[name=name]").val();
        if (userName=='') {
            $('.login-form__errors').text("¬ведите им€!");
            return false;
        }
        var userPassword = $("input[name=password]").val();
        if (userPassword=='') {
            $('.login-form__errors').text("¬ведите пароль!");
            return false;
        }
        return true;
    }
    function clearErrors(){
        $('.login-form__errors').text("");
    }

    return new View({content: '.page-login'});
});