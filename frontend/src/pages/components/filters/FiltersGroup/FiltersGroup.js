import React, { useState, useEffect } from 'react';
import ChipsDisplay from './ChipsDisplay';
import FilterSection from './FilterSection';
import FilterList from './FilterList';
import FormButtons from './FormButtons';
import { Collapse } from 'react-bootstrap';
import mediaTypes from '../../../../api/values/MediaTypes';
import mediaOrderBy from '../../../../api/values/MediaOrderBy';

const FiltersGroup = ({
                          genresList,
                          providersList,
                          type,
                          orderBy,
                          query,
                          searchBar,
                          initialSelectedGenres = [],
                          initialSelectedProviders = [],
                      }) => {
    const [openFilters, setOpenFilters] = useState(!searchBar);
    const [openGenres, setOpenGenres] = useState(false);
    const [openProviders, setOpenProviders] = useState(false);
    const [selectedGenres, setSelectedGenres] = useState(initialSelectedGenres);
    const [selectedProviders, setSelectedProviders] = useState(initialSelectedProviders);
    const [searchGenre, setSearchGenre] = useState('');
    const [searchProvider, setSearchProvider] = useState('');
    const [queryInput, setQueryInput] = useState('');


    useEffect(() => {

    }, );

    const handleChipRemove = (setFunction, item) => {
        setFunction((prev) => prev.filter((i) => i !== item));
    };

    const handleFilterSubmit = (e) => {
        e.preventDefault();
        console.log('Filters applied:', {
            type,
            orderBy,
            genres: selectedGenres.join(','),
            providers: selectedProviders.join(','),
            queryInput,
        });
    };

    const handleReset = () => {
        setSelectedGenres([]);
        setSelectedProviders([]);
    };

    return (
        <div>
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

            {searchBar && (
                <button
                    className="btn btn-success m-1"
                    onClick={() => setOpenFilters(!openFilters)}
                    aria-controls="filters"
                    aria-expanded={openFilters}
                >
                    <i className="bi bi-filter" />
                </button>
            )}

            <Collapse in={openFilters}>
                <div className="m-1 flex-column" style={{ width: '20vw' }} id="filters">
                    <form id="filter-form" onSubmit={handleFilterSubmit} className="mb-2 d-flex flex-column">
                        {query && <input type="hidden" name="query" value={query} />}

                        <div className="d-flex flex-row m-1">
                            <select name="type" className="form-select m-1">
                                <option selected={type === mediaTypes.TYPE_ALL} value={mediaTypes.TYPE_ALL.valueOf()}>All</option>
                                <option selected={type === mediaTypes.TYPE_TVSERIE} value={mediaTypes.TYPE_TVSERIE.valueOf()}>Series</option>
                                <option selected={type === mediaTypes.TYPE_MOVIE} value={mediaTypes.TYPE_MOVIE.valueOf()}>Movies</option>
                            </select>

                            <select name="orderBy" className="form-select m-1">
                                <option selected={orderBy === mediaOrderBy.NAME} value={mediaOrderBy.NAME}>Title</option>
                                <option selected={orderBy === mediaOrderBy.TOTAL_RATING} value={mediaOrderBy.TOTAL_RATING.valueOf()}>Total Rating</option>
                                <option selected={orderBy === mediaOrderBy.TMDB_RATING} value={mediaOrderBy.TMDB_RATING.valueOf()}>TMDB Rating</option>
                                <option selected={orderBy === mediaOrderBy.RELEASE_DATE} value={mediaOrderBy.RELEASE_DATE.valueOf()}>Release Date</option>
                            </select>
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
                                        prev.includes(genre) ? prev.filter((g) => g !== genre) : [...prev, genre]
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
                                        prev.includes(provider) ? prev.filter((p) => p !== provider) : [...prev, provider]
                                    )
                                }
                            />
                        </FilterSection>
                    </form>
                </div>
            </Collapse>
        </div>
    );
};

export default FiltersGroup;
