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
		
        events: {
            "click a": "hide"
        },
        render: function () {
            this.collection.add({
                name: 'Новый игрок',
                score : Math.floor(Math.random() * (5000))
            });
            var players  = this.collection.toJSON();
            this.$el.append(this.template(players));
        }

    });

    return new View({collection: playerCollection, content: '.page-scoreboard'});
});