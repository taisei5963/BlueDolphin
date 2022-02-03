/**
 * 実動時間を直接スライダー及び時間に反映を行う処理
 */

var range = [];
var time = [];
var button = [];
var over_rval = 600;

document.addEventListener('click', function(e){
    var btns = document.querySelectorAll('.table1_tr5_td6_button');
    for(var i=0; i<btns.length; i++){
        range[i] = document.getElementById('range_id_' + (i+1));
        time[i] = document.getElementById('time_id_' + (i+1));
        button[i] = document.getElementById('button_id_' + (i+1));
    }
    var target = e.target;
<<<<<<< HEAD
    if(target.tagName == 'button' && target.type == "button"){
=======
    if(target.tagName == 'BUTTON' && target.type == "button"){
>>>>>>> 4bd00e31ee62b9ada2d652191e6b8e9778f9cdcd
        switch(target.id){
            case 'button_id_1':
                if(button[0].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[0].style.backgroundColor = "#00BFFF";
                    button[0].style.borderColor = "#000";
                    button[0].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[0].value, button[0].name), button[0].name);
                } else {
                    button[0].style.backgroundColor = "#DCDCDC";
                    button[0].style.borderColor = "#000";
                    button[0].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[0].name), button[0].name);
                }
                break;
            case 'button_id_2':
                if(button[1].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[1].style.backgroundColor = "#00BFFF";
                    button[1].style.borderColor = "#000";
                    button[1].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[1].value, button[1].name), button[1].name);
                } else {
                    button[1].style.backgroundColor = "#DCDCDC";
                    button[1].style.borderColor = "#000";
                    button[1].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[1].name), button[1].name);
                }
                break;
            case 'button_id_3':
                if(button[2].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[2].style.backgroundColor = "#00BFFF";
                    button[2].style.borderColor = "#000";
                    button[2].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[2].value, button[2].name), button[2].name);
                } else {
                    button[2].style.backgroundColor = "#DCDCDC";
                    button[2].style.borderColor = "#000";
                    button[2].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[2].name), button[2].name);
                }
                break;
            case 'button_id_4':
                if(button[3].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[3].style.backgroundColor = "#00BFFF";
                    button[3].style.borderColor = "#000";
                    button[3].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[3].value, button[3].name), button[3].name);
                } else {
                    button[3].style.backgroundColor = "#DCDCDC";
                    button[3].style.borderColor = "#000";
                    button[3].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[3].name), button[3].name);
                }
                break;
            case 'button_id_5':
                if(button[4].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[4].style.backgroundColor = "#00BFFF";
                    button[4].style.borderColor = "#000";
                    button[4].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[4].value, button[4].name), button[4].name);
                } else {
                    button[4].style.backgroundColor = "#DCDCDC";
                    button[4].style.borderColor = "#000";
                    button[4].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[4].name), button[4].name);
                }
                break;
            case 'button_id_6':
                if(button[5].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[5].style.backgroundColor = "#00BFFF";
                    button[5].style.borderColor = "#000";
                    button[5].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[5].value, button[5].name), button[5].name);
                } else {
                    button[5].style.backgroundColor = "#DCDCDC";
                    button[5].style.borderColor = "#000";
                    button[5].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[5].name), button[5].name);
                }
                break;
            case 'button_id_7':
                if(button[6].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[6].style.backgroundColor = "#00BFFF";
                    button[6].style.borderColor = "#000";
                    button[6].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[6].value, button[6].name), button[6].name);
                } else {
                    button[6].style.backgroundColor = "#DCDCDC";
                    button[6].style.borderColor = "#000";
                    button[6].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[6].name), button[6].name);
                }
                break;
            case 'button_id_8':
                if(button[7].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[7].style.backgroundColor = "#00BFFF";
                    button[7].style.borderColor = "#000";
                    button[7].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[7].value, button[7].name), button[7].name);
                } else {
                    button[7].style.backgroundColor = "#DCDCDC";
                    button[7].style.borderColor = "#000";
                    button[7].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[7].name), button[7].name);
                }
                break;
            case 'button_id_9':
                if(button[8].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[8].style.backgroundColor = "#00BFFF";
                    button[8].style.borderColor = "#000";
                    button[8].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[8].value, button[8].name), button[8].name);
                } else {
                    button[8].style.backgroundColor = "#DCDCDC";
                    button[8].style.borderColor = "#000";
                    button[8].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[8].name), button[8].name);
                }
                break;
            case 'button_id_10':
                if(button[9].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[9].style.backgroundColor = "#00BFFF";
                    button[9].style.borderColor = "#000";
                    button[9].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[9].value, button[9].name), button[9].name);
                } else {
                    button[9].style.backgroundColor = "#DCDCDC";
                    button[9].style.borderColor = "#000";
                    button[9].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[9].name), button[9].name);
                }
                break;
            case 'button_id_11':
                if(button[10].style.backgroundColor == 'rgb(220, 220, 220)'){
                    button[10].style.backgroundColor = "#00BFFF";
                    button[10].style.borderColor = "#000";
                    button[10].style.borderRadius = "3px";
                    RangeReflection(TimeReflection(button[10].value, button[10].name), button[10].name);
                } else {
                    button[10].style.backgroundColor = "#DCDCDC";
                    button[10].style.borderColor = "#000";
                    button[10].style.borderRadius = "3px";
                    RangeReflection(TimeReflection("00:00", button[10].name), button[10].name);
                }
                break;
            default:
                break;
        }
    }
}, false);

function TimeReflection(tval, name){
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
        case 'button_name_1':
            if(tval < over_rval){
                range[0].value = tval;
                return range[0].value;
            } else {
                range[0].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_2':
            if(tval < over_rval){
                range[1].value = tval;
                return range[1].value;
            } else {
                range[1].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_3':
            if(tval < over_rval){
                range[2].value = tval;
                return range[2].value;
            } else {
                range[2].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_4':
            if(tval < over_rval){
                range[3].value = tval;
                return range[3].value;
            } else {
                range[3].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_5':
            if(tval < over_rval){
                range[4].value = tval;
                return range[4].value;
            } else {
                range[4].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_6':
            if(tval < over_rval){
                range[5].value = tval;
                return range[5].value;
            } else {
                range[5].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_7':
            if(tval < over_rval){
                range[6].value = tval;
                return range[6].value;
            } else {
                range[6].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_8':
            if(tval < over_rval){
                range[7].value = tval;
                return range[7].value;
            } else {
                range[7].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_9':
            if(tval < over_rval){
                range[8].value = tval;
                return range[8].value;
            } else {
                range[8].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_10':
            if(tval < over_rval){
                range[9].value = tval;
                return range[9].value;
            } else {
                range[9].value = over_rval;
                over_rval = tval;
                return over_rval;
            }
        case 'button_name_11':
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

function RangeReflection(rval, name){
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
        case 'button_name_1':
            time[0].value = rval;
            break;
        case 'button_name_2':
            time[1].value = rval;
            break;
        case 'button_name_3':
            time[2].value = rval;
            break;
        case 'button_name_4':
            time[3].value = rval;
            break;
        case 'button_name_5':
            time[4].value = rval;
            break;
        case 'button_name_6':
            time[5].value = rval;
            break;
        case 'button_name_7':
            time[6].value = rval;
            break;
        case 'button_name_8':
            time[7].value = rval;
            break;
        case 'button_name_9':
            time[8].value = rval;
            break;
        case 'button_name_10':
            time[9].value = rval;
            break;
        case 'button_name_11':
            time[10].value = rval;
            break;
        default:
            break;
    }
}