import React, { useEffect, useState } from "react";
import userApi from "../../../api/UserApi";
import mediaService from "../../../services/MediaService";
import {createSearchParams, useNavigate, useSearchParams} from "react-router-dom";
import PaginationButton from "../paginationButton/PaginationButton";
import listService from "../../../services/ListService";

function Reviews({ id, source }) {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [reviews, setReviews] = useState(undefined);
    const [reviewsLoading, setReviewsLoading] = useState(true);
    const [reviewsError, setReviewsError] = useState(null);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);


    useEffect(() => {
        async function getData() {
            try {
                let response;
                switch (source) {
                    case 'media':
                        response = await mediaService.getReviewsByMediaId(id,page);
                        break;

                    case 'list':
                        response = await listService.getMoovieListReviewsFromListId({
                            id : id,
                            pageNumber : page
                        });
                        break;

                    case 'user':
                        response = await userApi.getMovieReviewsFromUser(id,page);
                        source = 'profile';
                        break;
                    default:
                        throw new Error(`Unsupported source: ${source}`);
                }
                setReviews(response);
                setReviewsLoading(false);
            } catch (error) {
                setReviewsError(error);
                setReviewsLoading(false);
            }
        }
        getData();
    }, [id]);

    const handlePageChange = (newPage) => {
        setPage(newPage);
        navigate({
            pathname: `/${source}/${id}`,
            search: createSearchParams({
                page: newPage.toString(),
            }).toString(),
        });
    };


    if (reviewsLoading) return <div>Cargando reseñas...</div>;
    if (reviewsError) return <div>Error al cargar reseñas: {reviewsError.message}</div>;
    if (reviews.length === 0) return <div>No hay reseñas disponibles.</div>;

    return (
        <div className="reviews-container">
            <h2>Reseñas</h2>
            {reviews?.data?.length > 0 ? (
                reviews.data.map((review) => (
                    <div key={review.id} className="review">
                        <div className="review-header">
                            <strong>{review.username}</strong> - <small>{review.rating}</small>
                        </div>
                        <div className="review-content">{review.reviewContent}</div>
                    </div>
                ))
            ) : (
                <p>No se encontraron reseñas.</p>
            )}
            <div className="flex justify-center pt-4">
                {reviews?.data?.length > 0 && reviews.links?.last?.page > 1 && (
                    <PaginationButton
                        page={page}
                        lastPage={reviews.links.last.page}
                        setPage={handlePageChange}
                    />
                )}
            </div>
        </div>

    );
}

export default Reviews;
