'use strict';

var BigNumber = require('../../type/BigNumber');
var errorTransform = require('./error.transform').transform;
var isNumber = require('../../be.flo.roommateService.util/number').isNumber;
var argsToArray = require('../../be.flo.roommateService.util/array').argsToArray;

/**
 * Attach a transform function to math.max
 * Adds a property transform containing the transform function.
 *
 * This transform changed the last `dim` parameter of function max
 * from one-based to zero based
 * @param {Object} math
 */
module.exports = function (math) {

  var isCollection = math.collection.isCollection;

  var transform = function () {
    var args = argsToArray(arguments);

    // change last argument dim from one-based to zero-based
    if (args.length == 2 && isCollection(args[0])) {
      var dim = args[1];
      if (isNumber(dim)) {
        args[1] = dim - 1;
      }
      else if (dim instanceof BigNumber) {
        args[1] = dim.minus(1);
      }
    }

    try {
      return math.max.apply(math, args);
    }
    catch (err) {
      throw errorTransform(err);
    }
  };

  math.max.transform = transform;

  return transform;
};
