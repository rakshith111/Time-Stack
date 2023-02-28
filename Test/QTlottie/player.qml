import QtQuick 2.15
import Qt.labs.lottieqt 6.3

Item {
    width: 600
    height: 600

    property string Src: ""
        LottieAnimation {
            anchors.fill: parent
            loops: LottieAnimation.Infinite
            direction: LottieAnimation.Forward
            
            // working full_loading.json, small_play.json, loading.json, smal.json, stars.json, online_converted.json
            // play.json long time to load
            // rest of the json files not working

            source: Src

            onStatusChanged: {
                console.log("status", status);
                if (status === LottieAnimation.Ready)
                {
                    frameRate = 60;
                    gotoAndPlay(startFrame+1);
                    console.log('source', source)
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
