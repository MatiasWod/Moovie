import React, { useState, useEffect } from 'react';
import ChipsDisplay from './chipsDisplay';
import FilterSection from './filterSection';
import FilterList from './filterList';
import FormButtons from './formButtons';
import mediaTypes from '../../../../api/values/MediaTypes';
import mediaOrderBy from '../../../../api/values/MediaOrderBy';
import ProviderService from "../../../../services/ProviderService";
import GenreService from "../../../../services/GenreService";
import SortOrder from "../../../../api/values/SortOrder";

const FiltersGroup = ({
                          type,
                          sortOrder,
                          orderBy,
                          query,
                          searchBar,
                          initialSelectedGenres = [],
                          initialSelectedProviders = [],
                          submitCallback
}) => {
    const [openGenres, setOpenGenres] = useState(false);
    const [openProviders, setOpenProviders] = useState(false);
    const [searchGenre, setSearchGenre] = useState('');
    const [searchProvider, setSearchProvider] = useState('');

    const [selectedGenres, setSelectedGenres] = useState(initialSelectedGenres);
    const [selectedProviders, setSelectedProviders] = useState(initialSelectedProviders);
    const [queryInput, setQueryInput] = useState(query);
    const [sortOrderInput, setSortOrderInput] = useState(sortOrder || SortOrder.DESC )
    const [mediaTypeInput, setMediaTypeInput] = useState(type || mediaTypes.TYPE_ALL);
    const [mediaOrderByInput, setMediaOrderByInput] = useState(orderBy || mediaOrderBy.TOTAL_RATING);

    const [genresList, setGenresList] = useState([])
    const [providersList, setProvidersList] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState(null)


    useEffect(() => {
        async function getProviders() {
            try {
                setLoading(true);
                const response = await ProviderService.getAllProviders();
                const providerList = response.data.map(provider => ({
                    name: provider.providerName,
                    id: provider.providerId
                }));
                setProvidersList(providerList);
            } catch (error) {
                setError(error);
                setLoading(false);
            } finally {
                setLoading(false);
            }
        }

        async function getGenres() {
            try {
                setLoading(true);
                const response = await GenreService.getAllGenres();
                const genreList = response.data.map(genre => ({
                    name: genre.genreName,
                    id: genre.genreId
                }));
                setGenresList(genreList);
            } catch (error) {
                setError(error);
                setLoading(false);
            } finally {
                setLoading(false);
            }
        }

        getProviders();
        getGenres();
    }, []);

    const handleChipRemove = (setFunction, item) => {
        setFunction((prev) => prev.filter((i) => i !== item));
    };

    const handleFilterSubmit = (e) => {
        e.preventDefault();
        submitCallback({type: mediaTypeInput, sortOrder: sortOrderInput,
            orderBy: mediaOrderByInput, search: queryInput,
            selectedProviders: selectedProviders, selectedGenres: selectedGenres})
    };

    const handleReset = () => {
        setSelectedGenres([]);
        setSelectedProviders([]);
        setQueryInput("");
        setSortOrderInput(SortOrder.DESC);
        setMediaTypeInput(mediaTypes.TYPE_ALL);
        setMediaOrderByInput(mediaOrderBy.TOTAL_RATING)
        submitCallback({type: mediaTypeInput, sortOrder: sortOrderInput, orderBy: mediaOrderByInput, search: queryInput})
    };

    return (
        <div style={{width: "30vw"}}>
            <ChipsDisplay
                title="Genres"
                items={selectedGenres}
                onRemove={(genre) => handleChipRemove(setSelectedGenres, genre)}
            />
            <ChipsDisplay
                title="Providers"
                items={selectedProviders}
                onRemove={(provider) => handleChipRemove(setSelectedProviders, provider)}
            />

            { query &&
                (
                    <h4>Results for: {query}</h4>
                )
            }

            <div className="m-1 flex-column" id="filters">
                <form id="filter-form" onSubmit={handleFilterSubmit} className="mb-2 d-flex flex-column">
                    {query && <input type="hidden" name="query" value={query} />}

                    <div className="d-flex flex-row m-1">
                        <select name="type" className="form-select m-1" onChange={(e) => setMediaTypeInput(e.target.value)}>
                            <option selected={mediaTypeInput === mediaTypes.TYPE_ALL} value={mediaTypes.TYPE_ALL}>All</option>
                            <option selected={mediaTypeInput === mediaTypes.TYPE_TVSERIE} value={mediaTypes.TYPE_TVSERIE}>Series</option>
                            <option selected={mediaTypeInput === mediaTypes.TYPE_MOVIE} value={mediaTypes.TYPE_MOVIE}>Movies</option>
                        </select>

                        <select name="orderBy" className="form-select m-1" onChange={(e) => setMediaOrderByInput(e.target.value)}>
                            <option selected={mediaOrderByInput === mediaOrderBy.NAME} value={mediaOrderBy.NAME}>Title</option>
                            <option selected={mediaOrderByInput === mediaOrderBy.TOTAL_RATING} value={mediaOrderBy.TOTAL_RATING}>Total Rating</option>
                            <option selected={mediaOrderByInput === mediaOrderBy.TMDB_RATING} value={mediaOrderBy.TMDB_RATING}>TMDB Rating</option>
                            <option selected={mediaOrderByInput === mediaOrderBy.RELEASE_DATE} value={mediaOrderBy.RELEASE_DATE}>Release Date</option>
                        </select>

                        { sortOrderInput === SortOrder.ASC
                            ? <button type={"button"} onClick={()=>setSortOrderInput(SortOrder.DESC)} className={'btn btn-success bi bi-sort-up'}/>
                            : <button type={"button"} onClick={()=>setSortOrderInput(SortOrder.ASC)} className={'btn btn-success bi bi-sort-down'}/>
                        }
                    </div>

                    {searchBar && (
                        <div className={"m-1"}>
                            <input
                                type="search"
                                className="form-control m-1"
                                placeholder="Search..."
                                value={queryInput}
                                onChange={(e) => setQueryInput(e.target.value)}
                            />
                        </div>
                    )}


                    <FormButtons onApply={handleFilterSubmit} onReset={handleReset} />

                    <FilterSection
                        title="Genres"
                        isOpen={openGenres}
                        toggleOpen={() => setOpenGenres(!openGenres)}
                    >
                        <FilterList
                            searchValue={searchGenre}
                            onSearchChange={setSearchGenre}
                            items={genresList}
                            selectedItems={selectedGenres}
                            onToggleItem={(genre) =>
                                setSelectedGenres((prev) =>
                                    prev.includes(genre.id) ? prev.filter((g) => g.id !== genre.id) : [...prev, genre]
                                )
                            }
                        />
                    </FilterSection>

                    <FilterSection
                        title="Providers"
                        isOpen={openProviders}
                        toggleOpen={() => setOpenProviders(!openProviders)}
                    >
                        <FilterList
                            searchValue={searchProvider}
                            onSearchChange={setSearchProvider}
                            items={providersList}
                            selectedItems={selectedProviders}
                            onToggleItem={(provider) =>
                                setSelectedProviders((prev) =>
                                    prev.includes(provider.id) ? prev.filter((p) => p.id !== provider.id) : [...prev, provider]
                                )
                            }
                        />
                    </FilterSection>
                </form>
            </div>
        </div>
    );
};

export default FiltersGroup;
