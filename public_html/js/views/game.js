define([
    'backbone',
    'tmpl/game',
    'underscore',
    'views/base'
], function(
    Backbone,
    tmpl,
    Underscore,
    BaseView
){


    var View = BaseView.extend({
        name: 'game',
        template: tmpl,
        className: "game-view",
        events: {
            "click a": "hide",
        },
        child_init: function(){
            /*$.ajax({
                type: "GET",
                datatype: "json",
                url: "/res/tilemap.json",
                async: false,
                success: function(data){
                    
                }
            });*/
        },
        child_render: function(){
            this.canvas =  document.getElementById("game-map__canvas");

			gameField = this.canvas.getContext('2d');
            
       		this.canvas.width  = 960;     
            this.canvas.height = 576;
        },
        child_show: function(){
            this.player.status({
                success: function(data){
                    if(data.isLogin != 'true'){
                        Backbone.history.navigate('login', true);
                    }
                }
            });
            this.player.joinGame();
        }
    });

    return new View({content: '.page-game'});
});