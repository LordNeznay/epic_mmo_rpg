define([
    'backbone',
	'underscore',
    'tmpl/scoreboard',
	'collections/scores'
], function(
    Backbone,
	Underscore,
    tmpl,
	playerCollection
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "scoreboard-view",
        
		initialize: function () {
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
        render: function () {
            this.$el.html( this.template() );
			if (this.playerTemplate) {
				this.showHighscore(); 
			}
        },
        show: function () {
            this.render();
        },
        hide: function () {
			this.$el.empty();
        },
		showHighscore: function(){
			var players  = this.collection.toJSON();
            var playersHtml = Underscore.template( this.playerTemplate, {players: players});
            $('.highscores__list').html(playersHtml);
		}

    });

    return new View({collection: playerCollection, el: $('.page')});
});