define([
    'backbone',
    'underscore',
    'views/base',
    'tmpl/scoreboard',
    'tmpl/list_players',
    'collections/scores'
], function(
    Backbone,
    Underscore,
    BaseView,
    tmpl,
    tmpl_highscores,
    playerCollection
){

    var View = BaseView.extend({
        name: 'scoreboard',
        template: tmpl,
        collection: playerCollection,
		
        initialize : function(options) {
            View.__super__.initialize.call(this, {content: '.page-scoreboard'});
        },
        
        events: {
            "click a": "hide"
        },
        
        render: function () {
            View.__super__.render.call(this);
            var that = this;

            that.collection.on('loadScoreboard', function(players){
                that.$el.find('#list_highscores').html(tmpl_highscores(players));
            });

        },
        show: function(){
            this.collection.fetch();
            View.__super__.show.call(this);
        }

    });

    return new View({content: '.page-scoreboard'});
});