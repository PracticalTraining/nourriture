//app.controller("search", function($scope, $http) {
//	$scope.search = function() {
//		var keyword = {
//				foodname:$scope.submit
//		}
//		if ($scope.submit != "null") {
//			$http({
//				method : 'GET',
//				url : "ws/food/food/search",
//				params : keyword
//			}).success(function(data) {
//				if (!data.errorCode) {
//					$cookieStore.put("username", $scope.username);
//				} else {
//					$scope.hintShow = true;
//					$scope.hint = "what you search is not here!";
//				}
//			});
//		}
//	}
//});

app.controller("search", function($scope, $http) {
	$scope.search = function() {
		var keyword = {
			name : $scope.foodname
		}
		console.log(keyword);
		if ($scope.submit != "null") {
			$http({
				method : 'GET',
				url : "ws/food/searchByName",
				params : keyword
			}).success(function(data) {
				console.log(data);
				if (!data.errorCode) {
					$scope.hint = data;
					/*
					 * 添加你的代码 首先解析json 然后将原来的内容清空 将新的内容插进去
					 */
					var foods = data.foods;
					if (foods.length > 0) {
						for (i = 0; i < foods.length; i++) {
							console.log(foods[i].name)
						}
					}

				} else {
					$scope.hintShow = "Something wrong happened!";
					$scope.hint = data;
				}
			});
		}
	}
});