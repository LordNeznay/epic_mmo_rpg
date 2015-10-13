define([
    'backbone',
	'models/cell'
], function(
    Backbone,
	CellModel
){
	var Collection = Backbone.Collection.extend({
		model: CellModel,
		initialize: function(){

		}
	});

    return new Collection();
});