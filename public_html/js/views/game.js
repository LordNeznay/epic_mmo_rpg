define([
    'backbone',
    'tmpl/game',
	'underscore',
	'collections/gameField'
], function(
    Backbone,
    tmpl,
	Underscore,
	gameField
){

    var View = Backbone.View.extend({

        template: tmpl,
		className: "game-view",

		initialize: function(){
            $.ajax({url: "utils/field_cell.html",
                context: this,
                success: function(response) {
                    this.cellTemplate = response;
                    this.showGameField();
                }
            });
		},
		events: {
            "click a": "hide"
        },
        render: function () {
            this.$el.html( this.template() );
			if(this.cellTemplate){
				this.showGameField();
			}
        },
        show: function () {
			this.render();
        },
        hide: function () {
			this.$el.empty();
        },
		showGameField: function(){
			var cells = this.collection.toJSON();
			$('.test-load-tilemap').html(Underscore.template(this.cellTemplate, {cells: cells}));
		}

    });

    return new View({collection: gameField, el: $('.page')});
});