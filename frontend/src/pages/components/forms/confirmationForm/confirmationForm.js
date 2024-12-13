import React, { useState } from "react";
import "../formsStyle.css";

const ConfirmationForm = ({
                              service, // Servicio a utilizar
                              actionName, // Nombre de la acción (ej: "Eliminar", "Enviar")
                              onConfirm, // Función a ejecutar después de confirmar
                              onCancel,  // Función para cancelar la acción
                              serviceParams // Parámetros a enviar al servicio
                          }) => {
    const [postResponse, setPostResponse] = useState("");
    const [responseColor, setResponseColor] = useState("");
    const [finished, setFinished] = useState(false);

    const handleConfirm = async () => {
        try {
            const response = await service(...serviceParams);
            if (response.status === 200) {
                setPostResponse("Acción completada exitosamente.");
                setResponseColor("green");
                onConfirm?.();
            } else {
                setPostResponse(response.data.message || "Error al completar la acción.");
                setResponseColor("red");
            }
        } catch (error) {
            setPostResponse("Error al realizar la solicitud.");
            setResponseColor("red");
        }
        setFinished(true);
    };

    if (!finished) {
        return (
            <div className="overlay">
                <div className="box-review" style={{ textAlign: "center", width: "70%", padding: "3em", margin: "2em auto" }}>
                    <h2 style={{ marginBottom: "1.5em" }}>¿Estás seguro de que deseas {actionName}?</h2>
                    <div className="buttons" style={{ display: "flex", justifyContent: "center", gap: "1.5em" }}>
                        <button className="cancel" style={{ padding: "0.8em 1.5em" }} onClick={onCancel}>
                            Cancelar
                        </button>
                        <button className="submit confirm-button" style={{ padding: "0.8em 1.5em" }} onClick={handleConfirm} >
                            Confirmar
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="overlay">
            <div className="box-review">
                <h2 style={{ color: responseColor }}>{postResponse}</h2>
                <button className="cancel" onClick={onCancel}>
                    Cerrar
                </button>
            </div>
        </div>
    );
};

export default ConfirmationForm;
