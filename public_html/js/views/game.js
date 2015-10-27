define([
    'backbone',
    'tmpl/game',
    'underscore',
    'views/base',
    'views/surroundings'
], function(
    Backbone,
    tmpl,
    Underscore,
    BaseView,
    Surroundings
){


    var View = BaseView.extend({
        name: 'game',
        template: tmpl,
        className: "game-view",
        
        surroundings: new Surroundings(),
        
        events: {
            "click a": "hide",
        },
        child_render: function(){
            var that = this;
            that.surroundings.canvas =  document.getElementById("game-map__canvas");
            that.surroundings.gameField = that.surroundings.canvas.getContext('2d');
            
            that.surroundings.canvas.width  = 960;     
            that.surroundings.canvas.height = 576;
            //this.surroundings.render();
            this.surroundings.listenTo(this.player, "loadMap", function(_map){
                that.surroundings.map = JSON.parse(_map);
                that.surroundings.trigger("readyDraw");
                //console.log(that.map);
            });
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