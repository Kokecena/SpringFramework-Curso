// Call the dataTables jQuery plugin
$(document).ready(function() {
  cargarUsuarios();
  $('#usuarios').DataTable();
  actualizarEmailDelUsuario();
});

function actualizarEmailDelUsuario(){
    document.getElementById('txtEmailUsuario').outerHTML = localStorage.email;
}

async function cargarUsuarios(){
      const request = await fetch('api/usuarios', {
        method: 'GET',
        headers: getHeaders()
      });
      const usuarios = await request.json();
      console.log(usuarios);
      let listadoHTML = '';
      for(let usuario of usuarios){
        let telefono = usuario.telefono == null ? '':usuario.telefono;
        let btnDelete = '<a href="#" onclick="eliminarUsuario('+usuario.id+')" class="btn-danger btn-circle btn-sm"><i class="fas fa-trash"></i></a>';
        let usuarioHtml = '<tr>'+
                             '<td>' + usuario.id + '</td>'+
                             '<td>' + usuario.nombre + ' ' + usuario.apellido + '</td>' +
                             '<td>' + usuario.email + '</td>' +
                             '<td>' + telefono + '</td>' +
                             '<td>' + btnDelete +'</td>' +
                           '</tr>';
        listadoHTML += usuarioHtml;
      }

    document.querySelector('#usuarios tbody').outerHTML = listadoHTML;
}

async function eliminarUsuario(id){
    if(!confirm("¿Desea eliminar este usuario?")) return;
    const request = await fetch('api/usuarios/'+id, {
            method: 'DELETE',
            headers: getHeaders()
         });
    location.reload();
}

function getHeaders(){
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
    };
}