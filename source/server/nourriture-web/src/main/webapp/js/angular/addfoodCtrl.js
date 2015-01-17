app.controller("addfood", function($scope, $http) {
	$scope.addfood = function() {
		var keyword = {
			name : $scope.foodname,
			price:$scope.price,
			picture:$scope.picture,
			category:$scope.category,
			flavour:$scope.flavour,
			manufacturer:$scope.manufacturer,
			producelocation:$scope.producelocation,
			buylocation:$scope.buylocation
		}
		console.log(keyword);
		if (keyword != "null") {
			$http({
				method : 'POST',
				url : "ws/food",
				params : keyword
			}).success(function(data) {
				console.log(data);
				if (!data.errorCode) {
					$scope.hint = data;
					/*
					 * 添加你的代码 首先解析json 然后将原来的内容清空 将新的内容插进去
					 */

				} else {
					$scope.hintShow = "Something wrong happened!";
					$scope.hint = data;
				}
			});
		}
	}
});
