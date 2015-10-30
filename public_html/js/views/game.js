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
        child_init: function(){
            _.bindAll(this, 'playerMove');
            $(document).bind('keypress', this.playerMove);
        },
        child_render: function(){
            var that = this;
            that.surroundings.canvas =  document.getElementById("game-map__canvas");
            that.surroundings.gameField = that.surroundings.canvas.getContext('2d');
            
            that.surroundings.canvas.width  = 960;     
            that.surroundings.canvas.height = 576;

            this.surroundings.listenTo(this.player, "loadMap", function(_map){
                that.surroundings.map = JSON.parse(_map);
                that.surroundings.trigger("mapIsLoad");
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
            this.player.startGame();
        },
        child_hide: function(){
            this.player.leaveGame();
        },
        
        playerMove: function(ev){
            var sim = String.fromCharCode(ev.keyCode || ev.which || 0);
            var that = this;
            //console.log();
            switch(sim){
                case 'ц':
                case 'w':{
                    that.player.move("up");
                }; break;
                case 'ы':
                case 's':{
                    that.player.move("down");
                }; break;
                case 'ф':
                case 'a':{
                    that.player.move("left");
                }; break;
                case 'в':
                case 'd':{
                    that.player.move("right");
                }; break;
            }
        }
    });

    return new View({content: '.page-game'});
});