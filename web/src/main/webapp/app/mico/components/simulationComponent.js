'use strict';

app.directive('simulationComponent', ['$timeout', '$http', '$q', 'RobotService', function($timeout, $http, $q, RobotService) {
    
    return {
        restrict: 'E',
        replace: false,
        scope: true,
        template:   '<div class="simulation">' +
        '</div>',
        controller: function SimulationControllerFunction($scope,$element,$attrs){
            
        },
        compile: function CompilingFunction(tElement, tAttrs)
        {
            //can only manipulate DOM here (can't access scope yet)
            return function LinkingFunction(scope, element, attrs, ctrl) {
                
                var simulationHeight = scope.component.componentOptions.simulationOptions.simulationHeight;
                var simulationWidth = scope.component.componentOptions.simulationOptions.simulationWidth;
                
                //can access scope now
                var keyboard = new THREEx.KeyboardState();
                var robot = {};
                // Rotate an object around an arbitrary axis in object space
                var rotObjectMatrix;
                function rotateAroundObjectAxis(object, axis, radians) {
                    rotObjectMatrix = new THREE.Matrix4();
                    rotObjectMatrix.makeRotationAxis(axis.normalize(), radians);
                    
                    // old code for Three.JS pre r54:
                    // object.matrix.multiplySelf(rotObjectMatrix);      // post-multiply
                    // new code for Three.JS r55+:
                    object.matrix.multiply(rotObjectMatrix);
                    
                    // old code for Three.js pre r49:
                    // object.rotation.getRotationFromMatrix(object.matrix, object.scale);
                    // new code for Three.js r50+:
                    
                    
                    //object.rotation.setEulerFromRotationMatrix(object.matrix);
                    object.rotation.setFromRotationMatrix(object.matrix);
                }
                
                var rotWorldMatrix;
                // Rotate an object around an arbitrary axis in world space       
                function rotateAroundWorldAxis(object, axis, radians) {
                    rotWorldMatrix = new THREE.Matrix4();
                    rotWorldMatrix.makeRotationAxis(axis.normalize(), radians);
                    
                    // old code for Three.JS pre r54:
                    //  rotWorldMatrix.multiply(object.matrix);
                    // new code for Three.JS r55+:
                    rotWorldMatrix.multiply(object.matrix);                // pre-multiply
                    
                    object.matrix = rotWorldMatrix;
                    
                    // old code for Three.js pre r49:
                    // object.rotation.getRotationFromMatrix(object.matrix, object.scale);
                    // old code for Three.js pre r59:
                    // object.rotation.setEulerFromRotationMatrix(object.matrix);
                    // code for r59+:
                    object.rotation.setFromRotationMatrix(object.matrix);
                }
                
                var container;
                
                var camera, scene, renderer;
                
                var mouseX = 0, mouseY = 0;
                
                var windowHalfX = window.innerWidth / 2;
                var windowHalfY = window.innerHeight / 2;
                
                
                init();
                animate();
                
                
                function init() {
                    
                    //container = document.createElement( 'div' );
                    //document.body.appendChild( container );
                    //$element.find('.simulation').append(container);
                    
                    container = $(element).find('.simulation');
                    
                    camera = new THREE.PerspectiveCamera( 45, window.innerWidth / window.innerHeight, 1, 2000 );
                    camera.position.set(700,700,700);
                    camera.up = new THREE.Vector3(0,0,1);
                    camera.lookAt(new THREE.Vector3(0,0,0));
                    
                    //rotateAroundWorldAxis(camera,new THREE.Vector3(0,0,1),3.14);
                    camera.up = new THREE.Vector3(0,0,1);
                    camera.lookAt(new THREE.Vector3(0,0,0));
                    
                    scene = new THREE.Scene();
                    
                    var ambient = new THREE.AmbientLight( 0x121033 );
                    scene.add( ambient );
                    
                    var directionalLight = new THREE.DirectionalLight( 0xffeedd );
                    directionalLight.position.set( 0, 0, -1 );
                    scene.add( directionalLight );
                    
                    
                    var axisHelper = new THREE.AxisHelper(10000);
                    scene.add(axisHelper);
                    
                    //create geometries
                    robot.base = new THREE.Mesh(new THREE.CylinderGeometry(50,50,10,30,1,false), new THREE.MeshBasicMaterial({color: 0xff0003}));
                    robot.stand = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    robot.joint2 = new THREE.Mesh(new THREE.CylinderGeometry(15,15,15,20,1,false));
                    robot.link2 = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    robot.joint3 = new THREE.Mesh(new THREE.CylinderGeometry(15,15,15,20,1,false));
                    robot.link3 = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    robot.joint4 = new THREE.Mesh(new THREE.CylinderGeometry(15,15,15,20,1,false));
                    robot.link4 = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    robot.joint5 = new THREE.Mesh(new THREE.CylinderGeometry(15,15,15,20,1,false));
                    robot.link5 = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    robot.joint6 = new THREE.Mesh(new THREE.CylinderGeometry(15,15,15,20,1,false));
                    robot.link6 = new THREE.Mesh(new THREE.CubeGeometry(20,80,20,20,20,20));
                    
                    //set positions
                    robot.base.position.set(0,0,2);
                    robot.stand.position.set(0,52,0);
                    robot.joint2.position.set(0,52,0);
                    robot.link2.position.set(0,0,-52);
                    robot.joint3.position.set(0,-52,0);
                    robot.link3.position.set(0,0,52);
                    robot.joint4.position.set(0,52,0);
                    robot.link4.position.set(0,0,-52);
                    robot.joint5.position.set(0,-52,0);
                    robot.link5.position.set(0,0,52);
                    robot.joint6.position.set(0,52,0);
                    robot.link6.position.set(0,0,-52);
                    
                    //specify parent-child relationships
                    robot.joint6.add(robot.link6);
                    robot.link5.add(robot.joint6);
                    robot.joint5.add(robot.link5);
                    robot.link4.add(robot.joint5);
                    robot.joint4.add(robot.link4);
                    robot.link3.add(robot.joint4);
                    robot.joint3.add(robot.link3);
                    robot.link2.add(robot.joint3);
                    robot.joint2.add(robot.link2);
                    robot.stand.add(robot.joint2);
                    robot.base.add(robot.stand);
                    
                    robot.currentJointAngles = [0,0,0,0,0,0];
                    robot.newJointAngles = [];
                    
                    robot.pristine = true;
                    
                    rotateAroundWorldAxis(robot.link6, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.joint6, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.link5, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.joint5, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.link4, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.joint4, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.link3, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.joint3, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.link2, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.joint2, new THREE.Vector3(1,0,0),Math.PI/2);
                    rotateAroundWorldAxis(robot.base,new THREE.Vector3(1,0,0),Math.PI/2);
                    scene.add(robot.base);
                    
                    
                    renderer = new THREE.WebGLRenderer();
                    renderer.setSize(simulationWidth, simulationHeight);
                    container.append( renderer.domElement );
                    
                    document.addEventListener( 'mousemove', onDocumentMouseMove, false );
                    
                    //
                    
                    //window.addEventListener( 'resize', onWindowResize, false );
                    
                }
                
                /*function onWindowResize() {
                    
                    windowHalfX = window.innerWidth / 2;
                    windowHalfY = window.innerHeight / 2;
                    
                    camera.aspect = window.innerWidth / window.innerHeight;
                    camera.updateProjectionMatrix();
                    
                    renderer.setSize( window.innerWidth, window.innerHeight );
                    
                }*/
                
                function onDocumentMouseMove( event ) {
                    
                    mouseX = ( event.clientX - windowHalfX ) / 2;
                    mouseY = ( event.clientY - windowHalfY ) / 2;
                    
                }
                
                //
                
                function animate() {
                    
                    requestAnimationFrame( animate );
                    checkRotation();
                    render();
                    
                }
                function isNumber(n){
                    return typeof n == 'number' && !isNaN(n) && isFinite(n);
                }
                function checkRotation(){
                    var rotSpeed = 0.1;
                    var x = camera.position.x,
                        y = camera.position.y,
                        z = camera.position.z;
                    
                    if (keyboard.pressed("a")){ 
                        camera.position.x = x * Math.cos(rotSpeed) + y * Math.sin(rotSpeed);
                        camera.position.y = y * Math.cos(rotSpeed) - x * Math.sin(rotSpeed);
                    } else if (keyboard.pressed("d")){
                        camera.position.x = x * Math.cos(rotSpeed) - y * Math.sin(rotSpeed);
                        camera.position.y = y * Math.cos(rotSpeed) + x * Math.sin(rotSpeed);
                    } else if (keyboard.pressed("w")){
                        camera.position.y = y * Math.cos(rotSpeed) + z * Math.sin(rotSpeed);
                        camera.position.z = z * Math.cos(rotSpeed) - y * Math.sin(rotSpeed);
                    } else if (keyboard.pressed("s")){
                        camera.position.x = x * Math.cos(rotSpeed) - z * Math.sin(rotSpeed);
                        camera.position.z = z * Math.cos(rotSpeed) + x * Math.sin(rotSpeed);
                    } /*else if (keyboard.pressed("1+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint1/" + (robot.currentJointAngles[0] + 0.5));
                    } else if (keyboard.pressed("1+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint1/" + (robot.currentJointAngles[0] - 0.5));
                    } else if (keyboard.pressed("2+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint2/" + (robot.currentJointAngles[1] + 0.5));
                    } else if (keyboard.pressed("2+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint2/" + (robot.currentJointAngles[1] - 0.5));
                    } else if (keyboard.pressed("3+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint3/" + (robot.currentJointAngles[2] + 0.5));
                    } else if (keyboard.pressed("3+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint3/" + (robot.currentJointAngles[2] - 0.5));
                    } else if (keyboard.pressed("4+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint4/" + (robot.currentJointAngles[3] + 0.5));
                    } else if (keyboard.pressed("4+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint4/" + (robot.currentJointAngles[3] - 0.5));
                    } else if (keyboard.pressed("5+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint5/" + (robot.currentJointAngles[4] + 0.5));
                    } else if (keyboard.pressed("5+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint5/" + (robot.currentJointAngles[4] - 0.5));
                    } else if (keyboard.pressed("6+w")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint6/" + (robot.currentJointAngles[5] + 0.5));
                    } else if (keyboard.pressed("6+s")){
                        $.get("http://avogadro.beowulf.uwo.ca:8080/rosbridge/device/write/testDevice/joint6/" + (robot.currentJointAngles[5] - 0.5));
                    }*/
                    
                    camera.lookAt(scene.position);
                    
                } 
                
                
                function getCorrectedAngle(angle)
                {
                    var correctedAngle = angle % (2*Math.PI);;
                    if(correctedAngle < 0.0001 && correctedAngle > -0.00001)
                    {
                        correctedAngle=+0.0000;
                    }
                    correctedAngle = correctedAngle.toFixed(4);
                    
                    return correctedAngle;
                }
                
                function getAnglesSuccess(data)
                {
                    var items = [];
                    
                    if(robot.pristine)
                    {
                        $.each(data, function(key, val){
                            robot.newJointAngles.push(val);
                            var correctedAngle = getCorrectedAngle(val);
                            items.push( "<li id='" + key + "'>" + key + ": " + correctedAngle + "</li>" );
                        });
                        robot.pristine = false;
                    }
                    else
                    {
                        robot.currentJointAngles = robot.newJointAngles;
                        robot.newJointAngles = [];
                        
                        $.each(data, function(key, val){
                            robot.newJointAngles.push(val);
                            var correctedAngle = getCorrectedAngle(val);
                            items.push( "<li id='" + key + "'>" + key + ": " + correctedAngle + "</li>" );
                        });
                    }
                    
                    var joint1AngleDiff = robot.newJointAngles[0] - robot.currentJointAngles[0];
                    //if(joint1AngleDiff !== 0)
                    //{
                    rotateAroundWorldAxis(robot.base,new THREE.Vector3(0,0,1),joint1AngleDiff);
                    //}
                    var joint2AngleDiff = robot.newJointAngles[1] - robot.currentJointAngles[1];
                    //if(joint2AngleDiff !== 0)
                    //{
                    rotateAroundObjectAxis(robot.joint2,new THREE.Vector3(0,-1,0),joint2AngleDiff);
                    //}
                    
                    
                    var joint3AngleDiff = robot.newJointAngles[2] - robot.currentJointAngles[2];
                    //if(joint3AngleDiff !== 0)
                    //{
                    rotateAroundObjectAxis(robot.joint3,new THREE.Vector3(0,1,0),joint3AngleDiff);
                    //}
                    
                    var joint4AngleDiff = robot.newJointAngles[3] - robot.currentJointAngles[3];
                    //if(joint4AngleDiff !== 0)
                    //{
                    rotateAroundObjectAxis(robot.joint4,new THREE.Vector4(0,1,0),joint4AngleDiff);
                    //}
                    
                    var joint5AngleDiff = robot.newJointAngles[4] - robot.currentJointAngles[4];
                    //if(joint5AngleDiff !== 0)
                    //{
                    rotateAroundObjectAxis(robot.joint5,new THREE.Vector3(0,1,0),joint5AngleDiff);
                    //}
                    
                    var joint6AngleDiff = robot.newJointAngles[5] - robot.currentJointAngles[5];
                    //if(joint6AngleDiff !== 0)
                    //{
                    rotateAroundObjectAxis(robot.joint6,new THREE.Vector3(0,1,0),joint6AngleDiff);
                    //}
                    
                    
                    
                    
                    
                    
                    var list = $( "<ul/>", {
                        "class": "angles-list",
                        html: items.join( "" )
                    });
                    
                    $('.angles-list').replaceWith(list);
                    
                    renderer.render( scene, camera );
                }
                
                
                var counter = 1;
                
                function getPretendData()
                {
                    counter++
                    if(counter < 100)
                    {
                        var rotSpeed = 0.001;
                        
                        robot.currentJointAngles[0] += rotSpeed;
                        robot.currentJointAngles[1] += rotSpeed;
                        robot.currentJointAngles[2] += rotSpeed;
                    }
                    var data = {
                        "joint1": robot.currentJointAngles[0],
                        "joint2": robot.currentJointAngles[1],
                        "joint3": robot.currentJointAngles[2],
                        "joint4": 0,
                        "joint5": 0,
                        "joint6": 0
                    }
                    
                    
                    return data;
                }
                
                function ajaxSuccess(data)
                {
                    var correctlyOrderedData = {
                        "joint1": data.joint1,
                        "joint2": data.joint2,
                        "joint3": data.joint3,
                        "joint4": data.joint4,
                        "joint5": data.joint5,
                        "joint6": data.joint6,
                        "joint7": data.joint7,
                        "joint8": data.joint8
                    }
                    getAnglesSuccess(correctlyOrderedData);
                }
                
                function render() 
                {
                    
                    $.getJSON( localStorage.basePath + scope.component.componentOptions.url ,ajaxSuccess);
                    
                    $.ajax({
                      dataType: "jsonp",
                      url: localStorage.basePath +  scope.component.componentOptions.url,
                      data: data,
                      success: ajaxSuccess
                    });
                
                var data = 
                {
                    "joint1": 0,
                    "joint2": 0,
                    "joint3": 0,
                    "joint4": 0,
                    "joint5": 0,
                    "joint6": 0
                }; 
                getAnglesSuccess(data);
                
                // getAnglesSuccess(getPretendData());
            }
            }
        }
    }
}]);

