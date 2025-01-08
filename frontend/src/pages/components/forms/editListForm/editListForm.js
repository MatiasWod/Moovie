import React, { useState } from "react";
import "../formsStyle.css";
import listService from "../../../../services/ListService";

const EditListForm = ({ listName, listDescription, closeEdit, closeEditSuccess, listId }) => {
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [name, setName] = useState(listName);
    const [description, setDescription] = useState(listDescription);

    const handleSubmit = async () => {
        if (!name.trim()) {
            setError("El nombre de la lista no puede estar vacío.");
            return;
        }
        try {
            const response = await listService.editMoovieList(
                {
                    id: listId,
                    name: name,
                    description: description
                }
            );

            if (response.status === 200 || response.status === 201) {
                setSuccess(true);
            } else {
                setError(response.data.message || "Error al actualizar la lista.");
            }
        } catch (error) {
            setError(error.response?.data?.message || "Error al realizar la solicitud.");
        }
    };

    return (
        <div className="overlay">
            <div className="box-review">
                {success ? (
                    <>
                        <h2 style={{ color: "green" }}>¡Lista actualizada con éxito!</h2>
                        <button className="cancel" onClick={closeEditSuccess}>
                            Cerrar
                        </button>
                    </>
                ) : (
                    !error ? (
                        <>
                            <h2>Editar Lista</h2>
                            <div className="form-group">
                                <label htmlFor="list-name">Nombre de la Lista: </label>
                                <input
                                    id="list-name"
                                    placeholder={listName}
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    maxLength="100"
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="list-description">Descripción</label>
                                <textarea
                                    id="list-description"
                                    placeholder="Descripción (Opcional)"
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                    maxLength="500"
                                ></textarea>
                                <p>{description.length}/500</p>
                            </div>
                            <div className="buttons">
                                <button className="cancel" onClick={closeEdit}>
                                    Cancelar
                                </button>
                                <button
                                    className="submit"
                                    onClick={handleSubmit}
                                    disabled={!name.trim()}
                                >
                                    Guardar
                                </button>
                            </div>
                        </>
                    ) : (
                        <>
                            <h2 style={{ color: "red" }}>{error}</h2>
                            <button className="cancel" onClick={() => setError(null)}>
                                Volver
                            </button>
                        </>
                    )
                )}
            </div>
        </div>
    );

};

export default EditListForm;
