/**
 * ボタン押下による作業時間の５分増減処理
 */

var range = [];
var time = [];
var over_rval = 600;

document.addEventListener('click', function FiveMinutesUp(e){
    var plusbtn = document.querySelectorAll('.range_plus');
    for(var i=0; i<plusbtn; i++){
        range[i] = document.getElementById('range_id_' + (i+1));
        time[i] = document.getElementById('time_id_' + (i+1));
    }

    var target = e.target;
    if(target.tagName == 'BUTTON' && target.type == 'button'){
        switch(target.id){
            case 'plus_id_1':
                if(range[0].value < 600){
                    range[0].value = Number(range[0].value) + 5;
                    setCurrentValue(range[0].value, range[0].name);
                } else if(range[0].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[0].name);
                }
                break;
            case 'plus_id_2':
                if(range[1].value < 600){
                    range[1].value = Number(range[1].value) + 5;
                    setCurrentValue(range[1].value, range[1].name);
                } else if(range[1].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[1].name);
                }
                break;
            case 'plus_id_3':
                if(range[2].value < 600){
                    range[2].value = Number(range[2].value) + 5;
                    setCurrentValue(range[2].value, range[2].name);
                } else if(range[2].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[2].name);
                }
                break;
            case 'plus_id_4':
                if(range[3].value < 600){
                    range[3].value = Number(range[3].value) + 5;
                    setCurrentValue(range[1].value, range[3].name);
                } else if(range[3].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[3].name);
                }
                break;
            case 'plus_id_5':
                if(range[4].value < 600){
                    range[4].value = Number(range[4].value) + 5;
                    setCurrentValue(range[4].value, range[4].name);
                } else if(range[4].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[4].name);
                }
                break;
            case 'plus_id_6':
                if(range[5].value < 600){
                    range[5].value = Number(range[5].value) + 5;
                    setCurrentValue(range[5].value, range[5].name);
                } else if(range[5].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[5].name);
                }
                break;
            case 'plus_id_7':
                if(range[6].value < 600){
                    range[6].value = Number(range[6].value) + 5;
                    setCurrentValue(range[6].value, range[6].name);
                } else if(range[6].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[6].name);
                }
                break;
            case 'plus_id_8':
                if(range[7].value < 600){
                    range[7].value = Number(range[7].value) + 5;
                    setCurrentValue(range[7].value, range[7].name);
                } else if(range[7].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[7].name);
                }
                break;
            case 'plus_id_9':
                if(range[8].value < 600){
                    range[8].value = Number(range[8].value) + 5;
                    setCurrentValue(range[8].value, range[8].name);
                } else if(range[8].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[8].name);
                }
                break;
            case 'plus_id_10':
                if(range[9].value < 600){
                    range[9].value = Number(range[9].value) + 5;
                    setCurrentValue(range[9].value, range[9].name);
                } else if(range[9].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[1].name);
                }
                break;
            case 'plus_id_11':
                if(range[10].value < 600){
                    range[10].value = Number(range[10].value) + 5;
                    setCurrentValue(range[10].value, range[10].name);
                } else if(range[10].value >= 600){
                    over_rval = 5 + over_rval;
                    setCurrentValue(over_rval, range[10].name);
                }
                break;
            default:
                break;
        }
    }
}, false)

function FiveMinutesDown(){
    if(range.value <= 0){
        return;
    } else if(over_rval > 600){
        over_rval = over_rval - 5;
        setCurrentValue(over_rval);
    } else {
        range.value = Number(range.value) - 5;
        setCurrentValue(range.value);
    }
}

function setCurrentValue(rval, name) {
    if (rval < 60) {
        if (rval < 10) {
            rval = "00:0" + rval;
        } else {
            rval = "00:" + rval;
        }
    } else if (60 <= rval && rval < 120) {
        if ((rval - 60) < 10) {
            rval = "01:0" + (rval - 60);
        } else {
            rval = "01:" + (rval - 60);
        }
    } else if (120 <= rval && rval < 180) {
        if ((rval - 120) < 10) {
            rval = "02:0" + (rval - 120);
        } else {
            rval = "02:" + (rval - 120);
        }
    } else if (180 <= rval && rval < 240) {
        if ((rval - 180) < 10) {
            rval = "03:0" + (rval - 180);
        } else {
            rval = "03:" + (rval - 180);
        }
    } else if (240 <= rval && rval < 300) {
        if ((rval - 240) < 10) {
            rval = "04:0" + (rval - 240);
        } else {
            rval = "04:" + (rval - 240);
        }
    } else if (300 <= rval && rval < 360) {
        if ((rval - 300) < 10) {
            rval = "05:0" + (rval - 300);
        } else {
            rval = "05:" + (rval - 300);
        }
    } else if (360 <= rval && rval < 420) {
        if ((rval - 360) < 10) {
            rval = "06:0" + (rval - 360);
        } else {
            rval = "06:" + (rval - 360);
        }
    } else if (420 <= rval && rval < 480) {
        if ((rval - 420) < 10) {
            rval = "07:0" + (rval - 420);
        } else {
            rval = "07:" + (rval - 420);
        }
    } else if (480 <= rval && rval < 540) {
        if ((rval - 480) < 10) {
            rval = "08:0" + (rval - 480);
        } else {
            rval = "08:" + (rval - 480);
        }
    } else if (540 <= rval && rval < 600) {
        if ((rval - 540) < 10) {
            rval = "09:0" + (rval - 540);
        } else {
            rval = "09:" + (rval - 540);
        }
    } else if (600 <= rval && rval < 660) {
        if ((rval - 600) < 10) {
            rval = "10:0" + (rval - 600);
        } else {
            rval = "10:" + (rval - 600);
        }
    } else if (660 <= rval && rval < 720) {
        if ((rval - 660) < 10) {
            rval = "10:0" + (rval - 660);
        } else {
            rval = "10:" + (rval - 660);
        }
    } else if (720 <= rval && rval < 780) {
        if ((rval - 720) < 10) {
            rval = "10:0" + (rval - 780);
        } else {
            rval = "10:" + (rval - 780);
        }
    } else if (780 <= rval && rval < 840) {
        if ((rval - 600) < 10) {
            rval = "10:0" + (rval - 780);
        } else {
            rval = "10:" + (rval - 780);
        }
    } else if (840 <= rval && rval < 900) {
        if ((rval - 840) < 10) {
            rval = "10:0" + (rval - 840);
        } else {
            rval = "10:" + (rval - 840);
        }
    } else if (900 <= rval && rval < 960) {
        if ((rval - 900) < 10) {
            rval = "10:0" + (rval - 900);
        } else {
            rval = "10:" + (rval - 900);
        }
    } else if (960 <= rval && rval < 1020) {
        if ((rval - 960) < 10) {
            rval = "10:0" + (rval - 960);
        } else {
            rval = "10:" + (rval - 960);
        }
    } else if (1020 <= rval && rval < 1080) {
        if ((rval - 1020) < 10) {
            rval = "10:0" + (rval - 1020);
        } else {
            rval = "10:" + (rval - 1020);
        }
    } else if (1080 <= rval && rval < 1140) {
        if ((rval - 1080) < 10) {
            rval = "10:0" + (rval - 1080);
        } else {
            rval = "10:" + (rval - 1080);
        }
    } else if (1140 <= rval && rval < 1200) {
        if ((rval - 1140) < 10) {
            rval = "10:0" + (rval - 1140);
        } else {
            rval = "10:" + (rval - 1140);
        }
    } else if (1200 <= rval && rval < 1260) {
        if ((rval - 1200) < 10) {
            rval = "10:0" + (rval - 1200);
        } else {
            rval = "10:" + (rval - 1200);
        }
    }
    
    switch(name){
        case 'range_name_1':
            time[0].value = rval;
            break;
        case 'range_name_2':
            time[1].value = rval;
            break;
        case 'range_name_3':
            time[2].value = rval;
            break;
        case 'range_name_4':
            time[3].value = rval;
            break;
        case 'range_name_5':
            time[4].value = rval;
            break;
        case 'range_name_6':
            time[5].value = rval;
            break;
        case 'range_name_7':
            time[6].value = rval;
            break;
        case 'range_name_8':
            time[7].value = rval;
            break;
        case 'range_name_9':
            time[8].value = rval;
            break;
        case 'range_name_10':
            time[9].value = rval;
            break;
        case 'range_name_11':
            time[10].value = rval;
            break;
        default:
            break;
    }
}