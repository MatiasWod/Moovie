import React, {useEffect, useState} from "react";
import listApi from "../../api/ListApi";

function BrowseLists(){

    const [search, setSearch] = useState(null);
    const [ownerUsername, setOwnerUsername] = useState(null);
    const [type, setType] = useState(null);
    const [orderBy, setOrderBy] = useState(null);
    const [order, setOrder] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);


    const [mlcList, setMlcList] = useState([]);
    const [mlcListLoading, setMlcListLoading] = useState(true);
    const [mlcListError, setMlcListError] = useState(null);

    const fetchMlcList = async () => {
        try {
            const response = await listApi.getLists({
                params:{
                    search: search,
                    ownerUsername: ownerUsername,
                    type: type,
                    orderBy: orderBy,
                    order: order,
                    pageNumber: pageNumber
                }
            });
            setMlcList(response.data);
        } catch (err) {
            setMlcListError(err);
        } finally {
            setMlcListLoading(false);
        }
    };

    useEffect(() => {
        fetchMlcList();
    }, []);

    return (
        <div>
            <div>Lists browsing</div>
            <div>
                {mlcList.map(list => (
                    <div>{list.name}</div>
                ))}
            </div>
        </div>
)
    ;
}

export default BrowseLists;