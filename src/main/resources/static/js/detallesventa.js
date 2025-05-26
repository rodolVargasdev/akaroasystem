document.addEventListener('DOMContentLoaded', function () {
    // Manejar el botón de eliminación
    const deleteButtons = document.querySelectorAll('.product-action-delete');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const idVenta = this.getAttribute('data-id');
            const form = document.querySelector('#deleteVentaForm');
            form.setAttribute('action', `/venta/eliminar/${idVenta}`);
            // Mostrar el ID de la venta en el modal para confirmación
            const modalBody = document.querySelector('#eliminarVentaModal .modal-body');
            modalBody.querySelector('p').textContent =
                `¿Estás seguro de que deseas eliminar la venta #${idVenta}? Esta acción no se puede deshacer.`;
        });
    });

    // // Manejar el botón de edición
    const editButtons = document.querySelectorAll('.product-action-edit');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const idVenta = this.getAttribute('data-id');
            cargarDatosParaEdicion(idVenta);
        });
    });

    // Manejar el botón de guardar cambios
    document.getElementById('guardarCambiosBtn').addEventListener('click', function() {
        guardarCambiosVenta();
    });
});

// function obtenerDetallesVenta(idVenta) {
//     // Obtener información general de la venta
//     fetch('/venta/' + idVenta)
//         .then(response => response.json())
//         .then(venta => {
//             document.getElementById('ventaId').textContent = venta.idVenta;
//             document.getElementById('ventaFecha').textContent = venta.fechaVenta;
//             document.getElementById('ventaCliente').textContent = venta.idCliente.nombreCliente;
//             document.getElementById('ventaVendedor').textContent = venta.idUsuario.nombreCompleto;
//         });
//
//     // Obtener detalles de los productos vendidos
//     fetch('/venta/' + idVenta + '/detalles')
//         .then(response => response.json())
//         .then(detalles => {
//             const tbody = document.getElementById('detallesVentaBody');
//             tbody.innerHTML = '';
//             let total = 0;
//
//             detalles.forEach(detalle => {
//                 const subtotal = detalle.cantidad * detalle.precioUnitario;
//                 total += subtotal;
//
//                 const row = document.createElement('tr');
//                 row.innerHTML = `
//                         <td>${detalle.idProducto.nombreProducto}</td>
//                         <td>${detalle.cantidad}</td>
//                         <td>$${detalle.precioUnitario.toFixed(2)}</td>
//                         <td>$${subtotal.toFixed(2)}</td>
//                         <td>${detalle.lote ? detalle.lote.idLote : 'N/A'}</td>
//                     `;
//                 tbody.appendChild(row);
//             });
//
//             document.getElementById('ventaTotal').textContent = '$' + total.toFixed(2);
//         });
// }

function cargarDatosParaEdicion(idVenta) {
    // Obtener información general de la venta
    fetch('/venta/' + idVenta)
        .then(response => response.json())
        .then(venta => {
            document.getElementById('editVentaId').textContent = venta.idVenta;
            document.getElementById('editVentaFecha').textContent = venta.fechaVenta;
            document.getElementById('editVentaCliente').textContent = venta.idCliente.nombreCliente;
            document.getElementById('ventaIdHidden').value = venta.idVenta;

            // Obtener detalles de la venta
            return fetch('/venta/' + idVenta + '/detalles');
        })
        .then(response => response.json())
        .then(detalles => {
            const tbody = document.getElementById('detallesEdicionBody');
            tbody.innerHTML = '';

            detalles.forEach(detalle => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${detalle.idProducto.nombreProducto}</td>
                    <td>
                        <input type="number" 
                               name="detalle_${detalle.idDetalle}_cantidad"
                               value="${detalle.cantidad}"
                               min="1"
                               class="form-control cantidad-input">
                    </td>
                    <td>$${detalle.precioUnitario.toFixed(2)}</td>
                    <td>
                        <button type="button" 
                                class="btn btn-danger btn-sm btn-eliminar-detalle"
                                data-id="${detalle.idDetalle}">
                            Eliminar
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });

            // Agregar event listeners a los botones de eliminar
            document.querySelectorAll('.btn-eliminar-detalle').forEach(btn => {
                btn.addEventListener('click', function() {
                    const idDetalle = this.getAttribute('data-id');
                    const inputEliminados = document.getElementById('detallesAEliminar');
                    if (inputEliminados.value) {
                        inputEliminados.value += ',' + idDetalle;
                    } else {
                        inputEliminados.value = idDetalle;
                    }
                    this.closest('tr').remove();
                });
            });
        })
        .catch(error => {
            console.error('Error al cargar datos para edición:', error);
            alert('Error al cargar los datos de la venta');
        });
}

function guardarCambiosVenta() {
    const form = document.getElementById('editDetallesVentaForm');
    const formData = new URLSearchParams();
    const idVenta = document.getElementById('ventaIdHidden').value;

    // Agregar campos ocultos
    formData.append('ventaId', idVenta);
    formData.append('detallesAEliminar', document.getElementById('detallesAEliminar').value);

    fetch(`/venta/editar/${idVenta}`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // Cerrar el modal y recargar la página para mostrar mensajes
                const modal = bootstrap.Modal.getInstance(document.getElementById('editarDetallesVentaModal'));
                modal.hide();
                window.location.href = '/listaventas';
            } else {
                throw new Error('Error en la respuesta del servidor');
            }
        })
        .catch(error => {
            console.error('Error al guardar cambios:', error);
            alert('Error al guardar los cambios: ' + error.message);
        });
}