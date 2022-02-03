/**
工数関連全般の処理を行う
 */

/**
チェックボックスの項目全てにチェックを入れる処理
*/
function AllChecked(){
  var all_check = document.checkbox.all.checked;
  for (var i=0; i<document.checkbox.part.length; i++){
    document.checkbox.part[i].checked = all_check;
  }
}

/**
一つの項目でもチェックが外れた場合の処理
*/
function DisChecked(){
  var checks = document.checkbox.part;
  var checksCount = 0;
  for (var i=0; i<checks.length; i++){
    if(checks[i].checked == false){
      document.checkbox.all.checked = false;
    }else{
      checksCount += 1;
      if(checksCount == checks.length){
        document.checkbox.all.checked = true;
      }
    }
  }
}

/**
「クリア」リンク押下で選択項目のチェックをすべて解除する処理
*/
function allcheck(tf){
	var ElementsCount = document.checkbox.elements.length;
	for(i=0; i<ElementsCount; i++){
		document.checkbox.elements[i].checked = tf;
	}
}