document.addEventListener('DOMContentLoaded', function() {
    // Manejar clic en botones de detalles
    document.querySelectorAll('.detalles-btn').forEach(button => {
        button.addEventListener('click', function() {
            const idVenta = this.getAttribute('data-venta-id');
            mostrarDetallesVenta(idVenta);
        });
    });
});

function mostrarDetallesVenta(idVenta) {
    // Obtener información general de la venta
    fetch('/reportes/ventas/detalles?idVenta=' + idVenta)
        .then(response => response.json())
        .then(detalles => {
            // Primero, obtener la información de la venta principal desde el primer detalle
            const venta = detalles.length > 0 ? detalles[0].idVenta : null;

            if (venta) {
                document.getElementById('ventaIdHeader').textContent = venta.idVenta;
                document.getElementById('ventaFecha').textContent = new Date(venta.fechaVenta).toLocaleDateString();
                document.getElementById('ventaCliente').textContent = venta.idCliente ? venta.idCliente.nombreCliente : 'N/A';
                document.getElementById('ventaVendedor').textContent = venta.idUsuario ? venta.idUsuario.nombreCompleto : 'N/A';
            }

            // Llenar la tabla de productos
            const tbody = document.getElementById('detallesVentaBody');
            tbody.innerHTML = '';
            let total = 0;

            detalles.forEach(detalle => {
                const subtotal = detalle.cantidad * detalle.precioUnitario;
                total += subtotal;

                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${detalle.idProducto.nombreProducto}</td>
                    <td>${detalle.cantidad}</td>
                    <td>$${detalle.precioUnitario.toFixed(2)}</td>
                    <td>$${subtotal.toFixed(2)}</td>
                    <td>${detalle.lote ? detalle.lote.idLote : 'N/A'}</td>
                `;
                tbody.appendChild(row);
            });

            document.getElementById('ventaTotal').textContent = '$' + total.toFixed(2);

            // Mostrar el modal
            const modal = new bootstrap.Modal(document.getElementById('detallesVentaModal'));
            modal.show();
        })
        .catch(error => {
            console.error('Error al obtener detalles de venta:', error);
            alert('Error al cargar los detalles de la venta');
        });
}