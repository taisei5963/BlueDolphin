/**
 * 作業時間変換処理
 * スライダー　→　時間
 * 時間　→　スライダー
 */

var range = [];
var time = [];
var over_rval = 600;

window.onload = function(){

    var rgns = document.querySelectorAll('.table1_tr5_td1_range');
    for(var i=0; i<rgns.length; i++){
        range[i] = document.getElementById('range_id_' + (i+1));
        time[i] = document.getElementById('time_id_' + (i+1));
    }

    if(range[0] != null && range[0].name == 'range_name_1'){
        range[0].addEventListener('input', rangeOnChange);
    }
    if(time[0] != null && time[0].name == 'time_name_1'){
        time[0].addEventListener('input', timeOnChange);
    }

    if(range[1] != null && range[1].name == 'range_name_2'){
        range[1].addEventListener('input', rangeOnChange);
    }
    if(time[1] != null && time[1].name == 'time_name_2'){
        time[1].addEventListener('input', timeOnChange);
    }

    if(range[2] != null && range[2].name == 'range_name_3'){
        range[2].addEventListener('input', rangeOnChange);
    }
    if(time[2] != null && time[2].name == 'time_name_3'){
        time[2].addEventListener('input', timeOnChange);
    }

    if(range[3] != null && range[3].name == 'range_name_4'){
        range[3].addEventListener('input', rangeOnChange);
    }
    if(time[3] != null && time[3].name == 'time_name_4'){
        time[3].addEventListener('input', timeOnChange);
    }

    if(range[4] != null && range[4].name == 'range_name_5'){
        range[4].addEventListener('input', rangeOnChange);
    }
    if(time[4] != null && time[4].name == 'time_name_5'){
        time[4].addEventListener('input', timeOnChange);
    }

    if(range[5] != null && range[5].name == 'range_name_6'){
        range[5].addEventListener('input', rangeOnChange);
    }
    if(time[5] != null && time[5].name == 'time_name_6'){
        time[5].addEventListener('input', timeOnChange);
    }

    if(range[6] != null && range[6].name == 'range_name_7'){
        range[6].addEventListener('input', rangeOnChange);
    }
    if(time[6] != null && time[6].name == 'time_name_7'){
        time[6].addEventListener('input', timeOnChange);
    }

    if(range[7] != null && range[7].name == 'range_name_8'){
        range[7].addEventListener('input', rangeOnChange);
    }
    if(time[7] != null && time[7].name == 'time_name_8'){
        time[7].addEventListener('input', timeOnChange);
    }

    if(range[8] != null && range[8].name == 'range_name_9'){
        range[8].addEventListener('input', rangeOnChange);
    }
    if(time[8] != null && time[8].name == 'time_name_9'){
        time[8].addEventListener('input', timeOnChange);
    }

    if(range[9] != null && range[9].name == 'range_name_10'){
        range[9].addEventListener('input', rangeOnChange);
    }
    if(time[9] != null && time[9].name == 'time_name_10'){
        time[9].addEventListener('input', timeOnChange);
    }

    if(range[10] != null && range[10].name == 'range_name_11'){
        range[10].addEventListener('input', rangeOnChange);
    }
    if(time[10] != null && time[10].name == 'time_name_11'){
        time[10].addEventListener('input', timeOnChange);
    }
}

function rangeOnChange(e){
    setCurrentValue(e.target.value, e.target.name);
}

function timeOnChange(e){
    setCurrentTimeValue(e.target.value, e.target.name);
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
            rval = "11:0" + (rval - 660);
        } else {
            rval = "11:" + (rval - 660);
        }
    } else if (720 <= rval && rval < 780) {
        if ((rval - 720) < 10) {
            rval = "12:0" + (rval - 720);
        } else {
            rval = "12:" + (rval - 720);
        }
    } else if (780 <= rval && rval < 840) {
        if ((rval - 780) < 10) {
            rval = "13:0" + (rval - 780);
        } else {
            rval = "13:" + (rval - 780);
        }
    } else if (840 <= rval && rval < 900) {
        if ((rval - 840) < 10) {
            rval = "14:0" + (rval - 840);
        } else {
            rval = "14:" + (rval - 840);
        }
    } else if (900 <= rval && rval < 960) {
        if ((rval - 900) < 10) {
            rval = "15:0" + (rval - 900);
        } else {
            rval = "15:" + (rval - 900);
        }
    } else if (960 <= rval && rval < 1020) {
        if ((rval - 960) < 10) {
            rval = "16:0" + (rval - 960);
        } else {
            rval = "16:" + (rval - 960);
        }
    } else if (1020 <= rval && rval < 1080) {
        if ((rval - 1020) < 10) {
            rval = "17:0" + (rval - 1020);
        } else {
            rval = "17:" + (rval - 1020);
        }
    } else if (1080 <= rval && rval < 1140) {
        if ((rval - 1080) < 10) {
            rval = "18:0" + (rval - 1080);
        } else {
            rval = "18:" + (rval - 1080);
        }
    } else if (1140 <= rval && rval < 1200) {
        if ((rval - 1140) < 10) {
            rval = "19:0" + (rval - 1140);
        } else {
            rval = "19:" + (rval - 1140);
        }
    } else if (1200 <= rval && rval < 1260) {
        if ((rval - 1200) < 10) {
            rval = "20:0" + (rval - 1200);
        } else {
            rval = "20:" + (rval - 1200);
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

function setCurrentTimeValue(tval, name){
    var tval_h = 0;
    var tval_m = 0;
    if(tval < '10:00'){
        tval_h = parseInt(tval.substring(1,2)) * 60;
        if(parseInt(tval.substring(3,4)) == 0 && parseInt(tval.substring(4,5)) < 10){
            tval_m = parseInt(tval.substring(4,5));
        }
        if(parseInt(tval.substring(3,5)) >= 10){
            tval_m = parseInt(tval.substring(3,5));
        }
    } else if(tval >= '10:00'){
        tval_h = parseInt(tval.substring(0,2)) * 60;
        if(parseInt(tval.substring(3,4)) == 0 && parseInt(tval.substring(4,5)) < 10){
            tval_m = parseInt(tval.substring(4,5));
        }
        if(parseInt(tval.substring(3,5)) >= 10){
            tval_m = parseInt(tval.substring(3,5));
        }
    }
    tval = tval_h + tval_m;
    
    switch(name){
        case 'time_name_1':
            if(tval < over_rval){
                range[0].value = tval;
                return range[0].value;
            } else {
                range[0].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_2':
            if(tval < over_rval){
                range[1].value = tval;
                return range[1].value;
            } else {
                range[1].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_3':
            if(tval < over_rval){
                range[2].value = tval;
                return range[2].value;
            } else {
                range[2].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_4':
            if(tval < over_rval){
                range[3].value = tval;
                return range[3].value;
            } else {
                range[3].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_5':
            if(tval < over_rval){
                range[4].value = tval;
                return range[4].value;
            } else {
                range[4].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_6':
            if(tval < over_rval){
                range[5].value = tval;
                return range[5].value;
            } else {
                range[5].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_7':
            if(tval < over_rval){
                range[6].value = tval;
                return range[6].value;
            } else {
                range[6].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_8':
            if(tval < over_rval){
                range[7].value = tval;
                return range[7].value;
            } else {
                range[7].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_9':
            if(tval < over_rval){
                range[8].value = tval;
                return range[8].value;
            } else {
                range[8].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_10':
            if(tval < over_rval){
                range[9].value = tval;
                return range[9].value;
            } else {
                range[9].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'time_name_11':
            if(tval < over_rval){
                range[10].value = tval;
                return range[10].value;
            } else {
                range[10].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        default:
            break;
    }
}