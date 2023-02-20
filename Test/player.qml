
import QtQuick 2.15
import Qt.labs.lottieqt 6.3


Item {
    width: 600
    height: 600
    LottieAnimation {
        anchors.fill: parent
        loops: LottieAnimation.Infinite
        direction: LottieAnimation.Forward
        // working small_play.json,loading.json,smal.json
        // play.json long time to load 
        // rest of the json files not working
        source: "http://127.0.0.1:8000/full_loading.json"
        onStatusChanged : {
            if (status === LottieAnimation.Ready)
            {
                frameRate = 60;
                gotoAndPlay(startFrame);
                console.log("Ready")
                console.log("startFrame: " + startFrame)
                console.log("endFrame: " + endFrame)
                console.log("frameRate: " + frameRate)
                console.log("direction: " + direction)
                console.log("loops: " + loops)

            }
        }

    }
}

