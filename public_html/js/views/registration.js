define([
    'backbone',
    'tmpl/registration'
], function(
    Backbone,
    tmpl
){
    

    var View = Backbone.View.extend({

        template: tmpl,
        className: "login-registration",

        events: {
            "submit .signup-form__form": "submitSignup",
            "click a": "hide"
        },
        render: function () {
            this.$el.html( this.template() );
        },
        show: function () {
            this.render();
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
        hide: function () {
            this.$el.empty();
        },
        submitSignup: function(){
            clearErrors();
            if(validateForm()){
                var pView = this;
                var values = $('.signup-form__form').serialize();
                $.ajax({
                    type: "POST",
                    data: values,
                    url: "/api/v1/auth/signup",
                    dataType: 'json',
                    success: function(data) {
                        if(data.errors == 'null'){
                            $.post("/api/v1/auth/signin", values);
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
            $('.signup-form__errors').text("вЂ“РўвЂ“в‰¤вЂ“ВµвЂ“С–вЂ“Р„вЂ”Р’вЂ“Вµ вЂ“Р„вЂ“Р‰вЂ”Рџ!");
            return false;
        }
        var userPassword = $("input[name=password]").val();
        if (userPassword=='') {
            $('.signup-form__errors').text("вЂ“РўвЂ“в‰¤вЂ“ВµвЂ“С–вЂ“Р„вЂ”Р’вЂ“Вµ вЂ“СљвЂ“в€ћвЂ”РђвЂ“РЉвЂ“С—вЂ”Рњ!");
            return false;
        }
        return true;
    }
    function clearErrors(){
        $('.signup-form__errors').text("");
    }

    return new View({el: $('.page')});
});