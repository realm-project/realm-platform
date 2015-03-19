'use strict';

app.directive('poseVisualizationComponent', ['$timeout', '$http', '$q', 'RobotService', function ($timeout, $http, $q, RobotService) {

    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template: '<div class="pose-visualization-component__container"> ' +

            '<div ng-hide="savedReplayedTrajectories.length" class="pose-visualization-component__labelContainer" >No trajectory has been replayed yet!</div>'+

            '<div ng-show="savedReplayedTrajectories.length" class="pose-visualization-component__labelContainer">' +
                '<span class="pose-visualization-component__label">Trajectory:</span>' +
                '<select ng-model="trajectoryName" ng-change="updateName(trajectoryName)">' +
                    '<option ng-repeat="trajectory in savedReplayedTrajectories track by $index">{{trajectory.name}}</option>' +
                '</select>' +
            '</div>' +


            '<div ng-show="savedReplayedTrajectories.length" class="pose-visualization-component__labelContainer">' +
                '<span class="pose-visualization-component__label">Gains:</span>' +
                '<select ng-model="replayName" ng-change="updateReplay(replayName)">' +
                    '<option ng-repeat="replay in replays">{{replay.name}}</option>' +
                '</select>' +
            '</div>' +

            '<div ng-show="savedReplayedTrajectories.length" class="pose-visualization-component__labelContainer">' +
                '<span class="pose-visualization-component__label">Axis:</span>' +
                    '<select ng-model="cartesian" ng-change="updateCartesian(cartesian)">' +
                        '<option>X</option>' +
                        '<option>Y</option>' +
                        '<option>Z</option>' +
                    '</select>' +
            '</div>' +


            '<div class="pose-visualization-component__chartContainer">' +
                '<div class="pose-visualization-component__chart">' + '</div>' +
                '<a ng-show="savedReplayedTrajectories.length" class="trajectory-component__downloadBtn btn btn-primary"  ng-click="downloadTrajectory()">Download</a>'+
            '</div>' +
            '</div>',
        controller: function ControllerFunction($scope, $element, $attrs, $rootScope) {

            //all of the saved trajectories
            $scope.savedTrajectories = [];
            //saved trajectories that have been replayed
            $scope.savedReplayedTrajectories = [];
            //the current trajectory being shown
            $scope.trajectory = {};
            //replay set of the current trajectory
            $scope.replays = {};
            //the current replay element
            $scope.replay = {};

            //dummy data
            var trajectory1 = {
                id: 0,
                name: "Trajectory 1",
                teachPoints: [],
                replays: [
                    {
                        name: "Gains 1: (1,2,3)",
                        id: 1,
                        PID: {
                            P: 1,
                            D: 2,
                            I: 3
                        },
                        positions: [
                            {
                                timestamp: 1,
                                position: [
                                    {label: "x", value: 0},
                                    {label: "y", value: 2},
                                    {label: "z", value: 2}
                                ]

                            },
                            {
                                timestamp: 2,
                                position: [
                                    {label: "x", value: -0.8},
                                    {label: "y", value: 6},
                                    {label: "z", value: 10}
                                ]

                            },
                            {
                                timestamp: 3,
                                position: [
                                    {label: "x", value: 0.2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                            {
                                timestamp: 4,
                                position: [
                                    {label: "x", value: 0},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                        ]
                    },
                    //second replayyyyy
                    {
                        name: "Gains 2: (1,4,3)",
                        id: 2,
                        PID: {
                            P: 1,
                            D: 4,
                            I: 3
                        },
                        positions: [
                            {
                                timestamp: 1,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 2},
                                    {label: "z", value: 2}
                                ]

                            },
                            {
                                timestamp: 2,
                                position: [
                                    {label: "x", value: 4},
                                    {label: "y", value: 6},
                                    {label: "z", value: 10}
                                ]

                            },
                            {
                                timestamp: 3,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                            {
                                timestamp: 4,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                        ]
                    }
                ]
            };
            var trajectory2 = {
                id: 1,
                name: "Trajectory 2",
                teachPoints: [],
                replays: [
                    {
                        name: "Gains 1: (1,2,5)",
                        id: 1,
                        PID: {
                            P: 1,
                            D: 2,
                            I: 5
                        },
                        positions: [
                            {
                                timestamp: 1,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 2},
                                    {label: "z", value: 2}
                                ]

                            },
                            {
                                timestamp: 2,
                                position: [
                                    {label: "x", value: 4},
                                    {label: "y", value: 6},
                                    {label: "z", value: 10}
                                ]

                            },
                            {
                                timestamp: 3,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                            {
                                timestamp: 4,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                        ]
                    },
                    //second replayyyyy
                    {
                        name: "Gains 2: (1,4,5)",
                        id: 2,
                        PID: {
                            P: 1,
                            D: 4,
                            I: 5
                        },
                        positions: [
                            {
                                timestamp: 1,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 2},
                                    {label: "z", value: 2}
                                ]

                            },
                            {
                                timestamp: 2,
                                position: [
                                    {label: "x", value: 4},
                                    {label: "y", value: 6},
                                    {label: "z", value: 10}
                                ]

                            },
                            {
                                timestamp: 3,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                            {
                                timestamp: 4,
                                position: [
                                    {label: "x", value: 2},
                                    {label: "y", value: 1},
                                    {label: "z", value: 3}
                                ]

                            },
                        ]
                    }
                ]
            };
              // $scope.savedReplayedTrajectories.push(trajectory1);
             //  $scope.savedReplayedTrajectories.push(trajectory2);
            //------------------------------------------------

            //get information regarding the cartesian element of focus
            //gets the position array---> {timestamp , value} for x, y, or z
            $scope.getCartesian = function (positions, cartesian) {
                var cartesianArray = new Array();

                for (var i = 0; i < positions.length; i++) {
                    for (var j = 0; j < positions[i].position.length; j++) {
                        if (positions[i].position[j].label == cartesian.toLowerCase()) {
                            var position = new Object();
                            position.timestamp = positions[i].timestamp;
                            position.value = positions[i].position[j].value;
                            cartesianArray.push(position);
                        }
                    }
                }
                return cartesianArray;
            }
//-----------------------------------------------------------------
            //use d3 to draw the chart
            var draw = function (replay, cartesian) {
                //gets the position array---> {timestamp , value} for x, y, or z
                console.log("*******Now drawing..." + $scope.trajectory.name + " , " + $scope.replay.name);
                console.log("the replay instance:")
                console.log(replay);
                console.log("the cartesian: "+ cartesian)
                var positions = $scope.getCartesian(replay.positions, cartesian);
                console.log("the generated array:")
                console.log(positions)
                var positionArray = new Array()
                var timestampArray = new Array()
                for (var i = 0; i < positions.length; i++)
                    positionArray.push(positions[i].value);

                for (var i = 0; i < positions.length; i++)
                    timestampArray.push(positions[i].timestamp);

                d3.select("svg").remove();

                var width = 380;
                var height = 300;
                var widthMargin=20;
                var heightMargin=20;


                //finding the div element that chart should go in!
                /******it needs to be updated when template is changed****/
                var chart = $element.find("div")[6];

                var x = d3.scale.linear()
                    .domain([1, d3.max(timestampArray)]) //here we can add a number that is the max possible in general
                    .range([0, width - widthMargin]);

                var y = d3.scale.linear()
                    .domain([-70, 70])
                    .range([height - heightMargin, 0]);

                var xAxis = d3.svg.axis()
                    .scale(x)
                    .ticks(timestampArray.length)
                    .tickValues(timestampArray)
                    .tickFormat("")
                    //.tickFormat(d3.format(",.0f"))
                    .orient("bottom");

                var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left");

                var svg = d3.select(chart).append("svg")
                    .attr("width", width)
                    .attr("height", height);

                svg.append("g")
                    .attr("class", "axis")
                    .attr("transform","translate(0,"+(height-heightMargin)/2+")")
                    .call(xAxis)
                    .append("text")
                    .attr("y", 10)
                    .attr("x", width-widthMargin)
                    .style("text-anchor", "end")
                    .attr("dy", ".71em")
                    .text("Time");

                svg.append("g")
                    .attr("class", "axis")
                    .call(yAxis)
                    .append("text")
                    //.attr("transform", "rotate(-90)")
                    .attr("y", 6)
                    .attr("x", 6)
                    .attr("dy", ".71em")
                    //.style("text-anchor", "end")
                    .text(cartesian);


                var line = d3.svg.line()
                    .x(function (d) {
                        return x(d.timestamp);
                    })
                    .y(function (d) {
                        return y((d.value*100));
                    });

                svg.append("path")
                    .datum(positions)
                    .attr("class", "line")
                    .attr("d", line);


                svg.selectAll("dot")
                    .data(positions)
                    .enter().append("circle")
                    .attr("r", 2.5)
                    .attr("cx", function(d) { return x(d.timestamp); })
                    .attr("cy", function(d) { return y((d.value)*100); })
                    .attr("class","dot")
                    .append("title")
                    .text(function(d) { return ("Timestamp: "+d.timestamp+", "+cartesian+": "+(d.value*100).toFixed(4)); });


                //remove the first tick label
                svg.selectAll(".tick").selectAll("text")
                    .each(function (d, i) {
                        if (i == 0 && d == 1) {
                            this.remove();
                        }

                    });
                //moving x axis to the middle
            /*    svg.selectAll(".axis")
                    .each(function(d,i){
                           if(i==0) {
                              // console.log("common");
                                 d3.select(this).attr("transform","translate(0,"+(height-20)/2+")");
                           }
                    })*/
                console.log("-------------finished drawing")

            }
            //draw the initial chart based on default intialization of variable --> trajectory[0].replays[0]
            $scope.initialChart = function () {
                if ($scope.savedReplayedTrajectories.length !== 0) {
                    draw($scope.replay, $scope.cartesian);
                }
            }//end of initialChart
            //initialize the variables based on savedReplayedTrajectories set and draw the first chart
            $scope.initialize = function () {
                if ($scope.savedReplayedTrajectories.length != 0) {
                    $scope.trajectory = $scope.savedReplayedTrajectories[0];
                    $scope.trajectoryName = $scope.trajectory.name;
                    $scope.replays = $scope.trajectory.replays;
                    $scope.replay = $scope.trajectory.replays[0];
                    $scope.replayName = $scope.replay.name;
                    $scope.cartesian = "X";
                    $scope.initialChart();
                }
            }

            $scope.initialize();
//-------------------------------------------------------
            //when anything is changed in saved/replayed trajectory component: update savedTrajectories and savedReplayedTrajectories
            $scope.$on('savedTrajectoryUpdate', function (event, data) {
                console.log("pose visualization received the trajectories from save/replay: ")
                console.log(data);
                $scope.savedTrajectories=[];
                $scope.savedReplayedTrajectories=[];
                $scope.savedTrajectories = data;
                for (var i = 0; i < data.length; i++) {
                    if (data[i].replays.length != 0) {
                        for(var j=0;j<data[i].replays.length;j++)
                        {
                          var id=j+1;
                          data[i].replays[j].name="Gains "+id+" ("+data[i].replays[j].PID.P+","+data[i].replays[j].PID.I+","+data[i].replays[j].PID.D+")";
                        }
                        $scope.savedReplayedTrajectories.push(data[i])
                    }
                }
                $scope.initialize();
            });
//--------------------------------------------------------------
            //do the following when cartesian is changed in drop down menu
            $scope.updateCartesian = function (cartesian) {
                console.log(cartesian);
                draw($scope.replay, cartesian)
            }
            //do the following when trajectory name is updated
            $scope.updateName = function (name) {
                $scope.trajectory = $scope.getElementByName(name, $scope.savedReplayedTrajectories);
                $scope.replays = $scope.trajectory.replays;
                $scope.replay = $scope.replays[0];
                $scope.replayName = $scope.replay.name;
                draw($scope.replay, $scope.cartesian)
            }
            //do the following when replay is updated
            $scope.updateReplay = function (replay) {
                $scope.replay = $scope.getElementByName(replay, $scope.replays);
                draw($scope.replay, $scope.cartesian);
            }
//----------------------------------------------------------------
            //give a name of a trajectory or replay, return the instance of trajectory or replay
            $scope.getElementByName = function (name, array) {
                for (var i = 0; i < array.length; i++) {
                    if (array[i].name == name) {
                        return array[i];
                    }
                }
            }
//-----------------------------------------------------------------------
            $scope.downloadTrajectory=function()
            {

                var name=$scope.replay.name.replace(/ /g,"")+"_"+$scope.cartesian;
                JSONToCSVConvertor($scope.getCartesian($scope.replay.positions,$scope.cartesian), "Arm Positions", true,name)

               //the code for downloading JSON
              //  var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify($scope.replay.positions));
              //  var href="data:"+data;
              //  var download=name+".json"
              //  $(".trajectory-component__downloadBtn").attr("href",href);
              //  $(".trajectory-component__downloadBtn").attr("download",download);
            }
//-----------------------------------------------------------------------
            function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel,fileName) {
                //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
                var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
                var CSV = '';
                //Set Report title in first row or line
                CSV += ReportTitle + '\r\n\n';
                //This condition will generate the Label/Header
                if (ShowLabel) {
                    var row = "";

                    //This loop will extract the label from 1st index of on array
                    for (var index in arrData[0]) {

                        //Now convert each value to string and comma-seprated
                        row += index + ',';
                    }
                    row = row.slice(0, -1);
                    //append Label row with line break
                    CSV += row + '\r\n';
                }

                //1st loop is to extract each row
                for (var i = 0; i < arrData.length; i++) {
                    var row = "";

                    //2nd loop will extract each column and convert it in string comma-seprated
                    for (var index in arrData[i]) {
                        row += '"' + arrData[i][index] + '",';
                    }
                    row.slice(0, row.length - 1);
                    //add a line break after each row
                    CSV += row + '\r\n';
                }
                if (CSV == '') {
                    alert("Invalid data");
                    return;
                }
                //Generate a file name
                //var fileName = "MyReport_";
                //this will remove the blank-spaces from the title and replace it with an underscore
                //fileName += ReportTitle.replace(/ /g,"_");

                //Initialize file format you want csv or xls
                var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);

                // Now the little tricky part.
                // you can use either>> window.open(uri);
                // but this will not work in some browsers
                // or you will not get the correct file extension

                //this trick will generate a temp <a /> tag
                var link = document.createElement("a");
                link.href = uri;

                //set the visibility hidden so it will not effect on your web-layout
                link.style = "visibility:hidden";
                link.download = fileName + ".csv";

                //this part will append the anchor tag and remove it after automatic click
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }
//--------------------------------------------------------
        },
        compile: function CompilingFunction(tElement, tAttrs) {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                //can access scope now

            }
        },
        link: function (scope, element, attrs, trajectoryCtrl) {


        }//link


    }
}]);





