import React, {useEffect, useState} from 'react';
import Navbar from "../components/navBar/navbar";
import {useParams} from "react-router-dom";
import listApi from '../../api/ListApi'

function List() {

    const {id} = useParams();

    //GET VALUES FOR LIST
    const [list, setList] = useState([]);
    const [listLoading, setListLoading] = useState(true);
    const [listError, setListError] = useState(null);

    const fetchList = async () => {
        try {
            const response = await listApi.getListById(id);
            setList(response.data);
        } catch (err) {
            setListError(err);
        } finally {
            setListLoading(false);
        }
    };

    useEffect(() => {
        fetchList();
    }, []);


    return (
        <div>
            <Navbar/>
            <h1>Details Page</h1>
            <h1>{list.name}</h1>
            <h1>{list.id}</h1>
        </div>
    );
}

export default List;