define([
    'backbone',
    'underscore',
    'views/base',
    'tmpl/scoreboard',
    'collections/scores'
], function(
    Backbone,
    Underscore,
    BaseView,
    tmpl,
    playerCollection
){

    var View = BaseView.extend({
        name: 'scoreboard',
        template: tmpl,
        className: "scoreboard-view",
        
        child_init: function () {
            $.ajax({url: "utils/best_players.html",
                context: this,
                success: function(response) {
                    this.playerTemplate = response;
                    this.showHighscore();
                }
            });
        },
		
        events: {
            "click a": "hide"
        },
        child_render: function () {
            if (this.playerTemplate) {
                this.showHighscore(); 
            }
        },
        showHighscore: function(){
            var players  = this.collection.toJSON();
            var playersHtml = Underscore.template( this.playerTemplate, {players: players});
            $('.highscores__list').html(playersHtml);
        }

    });

    return new View({collection: playerCollection, content: '.page-scoreboard'});
});