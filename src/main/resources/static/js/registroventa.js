document.addEventListener('DOMContentLoaded', function () {
    const ventaForm = document.getElementById('ventaForm');
    const productoSelect = document.getElementById('producto');
    const cantidadInput = document.getElementById('cantidad');
    const precioUnitarioInput = document.getElementById('precioUnitario');
    const agregarBtn = document.getElementById('agregarProducto');
    const tablaProductos = document.getElementById('tablaProductos').getElementsByTagName('tbody')[0];
    const totalVentaSpan = document.getElementById('totalVenta');
    const guardarVentaBtn = document.getElementById('guardarVenta');
    const detallesContainer = document.getElementById('detallesContainer');

    // Actualizar precio unitario cuando se selecciona un producto
    productoSelect.addEventListener('change', function () {
        const selectedOption = this.options[this.selectedIndex];
        if (selectedOption.value) {
            precioUnitarioInput.value = selectedOption.getAttribute('data-precio');
            agregarBtn.disabled = false;
        } else {
            precioUnitarioInput.value = '';
            agregarBtn.disabled = true;
        }
    });

    // Agregar producto a la tabla
    agregarBtn.addEventListener('click', function () {
        const productoId = productoSelect.value;
        const productoNombre = productoSelect.options[productoSelect.selectedIndex].text.split(' - ')[0];
        const cantidad = parseInt(cantidadInput.value);
        const precioUnitario = parseFloat(precioUnitarioInput.value);
        const subtotal = cantidad * precioUnitario;

        // Verificar si el producto ya está en la tabla
        let productoExistente = false;
        const filas = tablaProductos.getElementsByTagName('tr');
        for (let fila of filas) {
            const idExistente = fila.getAttribute('data-producto-id');
            if (idExistente === productoId) {
                productoExistente = true;
                const cantidadCell = fila.cells[1];
                const cantidadActual = parseInt(cantidadCell.getElementsByTagName('input')[0].value);
                const nuevaCantidad = cantidadActual + cantidad;
                cantidadCell.getElementsByTagName('input')[0].value = nuevaCantidad;

                // Actualizar subtotal
                const subtotalCell = fila.cells[3];
                subtotalCell.textContent = (nuevaCantidad * precioUnitario).toFixed(2);
                break;
            }
        }

        if (!productoExistente) {
            // Crear nueva fila
            const nuevaFila = tablaProductos.insertRow();
            nuevaFila.setAttribute('data-producto-id', productoId);

            // Celda de producto
            const celdaProducto = nuevaFila.insertCell(0);
            celdaProducto.textContent = productoNombre;

            // Celda de cantidad
            const celdaCantidad = nuevaFila.insertCell(1);
            const cantidadInputCell = document.createElement('input');
            cantidadInputCell.type = 'number';
            cantidadInputCell.className = 'quantity-input';
            cantidadInputCell.value = cantidad;
            cantidadInputCell.min = '1';
            cantidadInputCell.addEventListener('change', actualizarTotal);
            celdaCantidad.appendChild(cantidadInputCell);

            // Celda de precio unitario
            const celdaPrecio = nuevaFila.insertCell(2);
            celdaPrecio.textContent = precioUnitario.toFixed(2);

            // Celda de subtotal
            const celdaSubtotal = nuevaFila.insertCell(3);
            celdaSubtotal.textContent = subtotal.toFixed(2);

            // Celda de acciones
            const celdaAcciones = nuevaFila.insertCell(4);
            const eliminarBtn = document.createElement('button');
            eliminarBtn.className = 'btn btn-danger btn-sm';
            eliminarBtn.innerHTML = '<i class="bi bi-trash"></i>';
            eliminarBtn.addEventListener('click', function () {
                nuevaFila.remove();
                actualizarTotal();
                actualizarDetallesOcultos();
                habilitarGuardarVenta();
            });
            celdaAcciones.appendChild(eliminarBtn);
        }

        // Actualizar total y detalles ocultos
        actualizarTotal();
        actualizarDetallesOcultos();
        habilitarGuardarVenta();

        // Limpiar selección
        productoSelect.selectedIndex = 0;
        precioUnitarioInput.value = '';
        cantidadInput.value = '1';
        agregarBtn.disabled = true;
    });

    // Función para actualizar el total
    function actualizarTotal() {
        let total = 0;
        const filas = tablaProductos.getElementsByTagName('tr');

        for (let fila of filas) {
            const cantidad = parseInt(fila.cells[1].getElementsByTagName('input')[0].value);
            const precioUnitario = parseFloat(fila.cells[2].textContent);
            const subtotal = cantidad * precioUnitario;
            fila.cells[3].textContent = subtotal.toFixed(2);
            total += subtotal;
        }

        totalVentaSpan.textContent = total.toFixed(2);
    }

    // Función para actualizar los detalles ocultos en el formulario
    function actualizarDetallesOcultos() {
        detallesContainer.innerHTML = ''; // Limpiar contenedor

        const filas = tablaProductos.getElementsByTagName('tr');

        Array.from(filas).forEach((fila, index) => {
            const productoId = fila.getAttribute('data-producto-id');
            const cantidad = fila.cells[1].getElementsByTagName('input')[0].value;
            const precioUnitario = fila.cells[2].textContent;

            // Crear inputs ocultos para cada propiedad del detalle
            const inputProductoId = document.createElement('input');
            inputProductoId.type = 'hidden';
            inputProductoId.name = `detalles[${index}].productoId`;
            inputProductoId.value = productoId;
            detallesContainer.appendChild(inputProductoId);

            const inputCantidad = document.createElement('input');
            inputCantidad.type = 'hidden';
            inputCantidad.name = `detalles[${index}].cantidad`;
            inputCantidad.value = cantidad;
            detallesContainer.appendChild(inputCantidad);

            const inputPrecio = document.createElement('input');
            inputPrecio.type = 'hidden';
            inputPrecio.name = `detalles[${index}].precioUnitario`;
            inputPrecio.value = precioUnitario;
            detallesContainer.appendChild(inputPrecio);
        });
    }

    // Función para habilitar/deshabilitar el botón de guardar venta
    function habilitarGuardarVenta() {
        guardarVentaBtn.disabled = tablaProductos.getElementsByTagName('tr').length === 0;
    }
});