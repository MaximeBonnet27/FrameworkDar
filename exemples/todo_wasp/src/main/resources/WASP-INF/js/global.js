var ajax;

function newTodo(){
	var content=document.getElementById("new-todo").value;
	document.getElementById("new-todo").value="";
	var pseudo=document.getElementById("pseudo").value;

	if (ajax && ajax.readyState !== 4) {
    			ajax.abort();
    }

	ajax = $.ajax({
			url: "http://localhost:1234/todo_wasp/todo?pseudo="+pseudo,
			async: false,
			timeout : 15000,
			type: "POST",
			dataType: "json",
			data:  { content : content },
			success: function(data) {
				addTodo(data.id,data.content,data.isDone);
			}
			});

return false;
}

function removeTodo(id){
	//appel api pour remove

var pseudo=document.getElementById("pseudo").value;

	if (ajax && ajax.readyState !== 4) {
    			ajax.abort();
    }

	ajax = $.ajax({
			url: "http://localhost:1234/todo_wasp/todo/"+id+"?pseudo="+pseudo,
			async: false,
			timeout : 15000,
			type: "DELETE",
			dataType: "json",
			success: function(data) {
				document.getElementById(id).remove();
			}
			});
}

function validate(id){
var pseudo=document.getElementById("pseudo").value;
	if(document.getElementById(id).classList.contains("validate")){
		if (ajax && ajax.readyState !== 4) {
            			ajax.abort();
            }

        	ajax = $.ajax({
        			url: "http://localhost:1234/todo_wasp/todo/"+id+"/unvalidate?pseudo="+pseudo,
        			async: false,
        			timeout : 15000,
        			type: "PUT",
        			dataType: "json",
        			success: function(data) {
        				document.getElementById(id).classList.remove("validate");
        			}
        			});
	}else{
		if (ajax && ajax.readyState !== 4) {
                    			ajax.abort();
                    }

                	ajax = $.ajax({
                			url: "http://localhost:1234/todo_wasp/todo/"+id+"/validate?pseudo="+pseudo,
                			async: false,
                			timeout : 15000,
                			type: "PUT",
                			dataType: "json",
                			success: function(data) {
                				document.getElementById(id).classList.add("validate");
                			}
                			});
	}
}

function addTodo(id,content,isDone){
	var li=document.createElement('li');
	li.setAttribute('id',id);

	var todoItem=document.createElement('div');
	todoItem.setAttribute('class','todoItem');

	var todo=document.createElement('todo');
	todo.setAttribute('class','todo');

	var check=document.createElement('input');
	check.setAttribute('class','check');
	check.setAttribute('type','checkbox');
	check.setAttribute('onClick','validate("'+id+'")');
	var label=document.createElement('label');
	label.innerHTML=content;

	todo.appendChild(check);
	todo.appendChild(label);

	var button=document.createElement('button');
	button.setAttribute('class','deleter');
	button.setAttribute('onClick','removeTodo("'+id+'")');
	button.innerHTML="X";

	todoItem.appendChild(todo);
	todoItem.appendChild(button);

	li.appendChild(todoItem);
	var element=document.getElementById("todolist");
	element.insertBefore(li,element.firstChild);

	if(isDone){
	document.getElementById(id).classList.add("validate");
	}else{
	document.getElementById(id).classList.remove("validate");
	}
}